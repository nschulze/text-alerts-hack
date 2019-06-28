package com.dwolla.text.alerts.domain.handlers

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.dwolla.eventbus.{Message, Topic}
import com.dwolla.text.alerts.domain.TextService

import scala.concurrent.Future

@Topic(name = "dwolla.financialinstitution.removed")
case class FinancialInstitutionRemoved(accountId: Option[String],
                                       timestamp: String,
                                       partnerAccountId: Option[UUID]) {
}

object FinancialInstitutionRemovedHandler {
  def props(m: Message, t: TextService) = Props(new AchRecordCreatedHandler(m, t))
}

class FinancialInstitutionRemovedHandler(msg: Message,
                              textService: TextService,
                             ) extends Actor with ActorLogging {

  private val event = msg.event.asInstanceOf[FinancialInstitutionRemoved]

  event match {
    case FinancialInstitutionRemoved(_, _, _) ⇒
      sendText("This is the bank removed message.")
    case _ ⇒ Future.successful(msg.ack())
  }

  private def sendText(text: String) = {
    textService.sendMessage(text)
  }

  def receive: Receive = { case _ ⇒ }
}
