package com.dwolla.text.alerts

import akka.actor.ActorSystem
import com.dwolla.eventbus.{EventBus, HandlerDefinition, Message}
import com.dwolla.server.{DataMigration, DwollaServer}
import com.dwolla.text.alerts.domain.TextService
import com.dwolla.text.alerts.domain.handlers.{AchRecordCreated, AchRecordCreatedHandler, FinancialInstitutionAdded, FinancialInstitutionAddedHandler, FinancialInstitutionRemoved, FinancialInstitutionRemovedHandler, TransferCompletedEvent, TransferCompletedEventHandler}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._

object Boot extends DwollaServer with DataMigration {
  val log: Logger = LoggerFactory.getLogger(getClass)

  val retryPolicy: Seq[FiniteDuration] = (1 to 5).map(_ ⇒ 100.milliseconds) ++ (1 to 5).map(_ ⇒ 5.seconds) ++ (1 to 5).map(_ ⇒ 1.minute)

  def main(): Unit = {
    val name = config.getString("name")
    val port = config.getInt("port")
    log.info(s"Starting $name")

      implicit val system: ActorSystem = ActorSystem()

      val bus = EventBus(name)

      val textService = new TextService()
      val createdHandler: HandlerDefinition = (m: Message) ⇒ AchRecordCreatedHandler.props(m, textService)
      val addedHandler: HandlerDefinition = (m: Message) ⇒ FinancialInstitutionAddedHandler.props(m, textService)
      val removedHandler: HandlerDefinition = (m: Message) ⇒ FinancialInstitutionRemovedHandler.props(m, textService)
      val completedHandler: HandlerDefinition = (m: Message) ⇒ TransferCompletedEventHandler.props(m, textService)

      bus
        .subscribe[AchRecordCreated](createdHandler)
        .subscribe[FinancialInstitutionAdded](addedHandler)
        .subscribe[FinancialInstitutionRemoved](removedHandler)
        .subscribe[TransferCompletedEvent](completedHandler)
        .withRetryPolicy(retryPolicy: _*)
        .prefetch(5)

      bus.start(system)


      registerHealthCheckBool(s"Connected to $name", () ⇒ true)

    log.info(s"Started $name on port $port")

    ()
  }
}
