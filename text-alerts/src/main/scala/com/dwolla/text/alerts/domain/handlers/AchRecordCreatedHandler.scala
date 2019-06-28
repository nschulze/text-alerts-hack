package com.dwolla.text.alerts.domain.handlers

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.dwolla.eventbus.{Message, Topic}
import com.dwolla.text.alerts.domain.TextService

import scala.concurrent.Future

@Topic(name = "dwolla.achrecord.created")
case class AchRecordCreated(transactionId: Int, displayId: UUID, displayType: String, accountId: Option[UUID], partnerAccountId: Option[UUID]) {
  def isDisplayable: Boolean  = !displayType.equalsIgnoreCase("NonDisplay")
}

object AchRecordCreatedHandler {
  def props(m: Message, t: TextService) = Props(new AchRecordCreatedHandler(m, t))
}

class AchRecordCreatedHandler(msg: Message,
                              textService: TextService,
                             ) extends Actor with ActorLogging {

  private val event = msg.event.asInstanceOf[AchRecordCreated]

  event match {
    case ach@AchRecordCreated(_, _, d, _, _) if ach.isDisplayable ⇒
      sendText(d)
    case _ ⇒ Future.successful(msg.ack())
  }

  private def sendText(text: String) = {
    textService.sendMessage(text)
  }

  def receive: Receive = { case _ ⇒ }
}
