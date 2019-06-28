package com.dwolla.text.alerts.domain.handlers

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.dwolla.eventbus.{Message, Topic}
import com.dwolla.text.alerts.domain.TextService

import scala.concurrent.Future

@Topic(name = "dwolla.financialinstitution.added")
case class FinancialInstitutionAdded(financialInstitutionId: UUID,
                                     userId: UUID,
                                     timestamp: String,
                                     workflow: String,
                                     accountId: UUID,
                                     partnerAccountId: Option[UUID],
                                     name: String,
                                     `type`: Option[String])

object FinancialInstitutionAddedHandler {
  def props(m: Message, t: TextService) = Props(new FinancialInstitutionAddedHandler(m, t))
}

class FinancialInstitutionAddedHandler(msg: Message,
                              textService: TextService,
                             ) extends Actor with ActorLogging {

  private val event = msg.event.asInstanceOf[FinancialInstitutionAdded]

  event match {
    case FinancialInstitutionAdded(_, _, _, _, _, _, _, _) ⇒
      sendText("This is the bank added message.")
    case _ ⇒ Future.successful(msg.ack())
  }

  private def sendText(text: String) = {
    textService.sendMessage(text)
  }

  def receive: Receive = { case _ ⇒ }
}
