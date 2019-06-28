package com.dwolla.text.alerts.domain.handlers

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.dwolla.eventbus.{Message, Topic}
import com.dwolla.text.alerts.domain.TextService

import scala.concurrent.Future

@Topic(name = "dwolla.transfer.completed")
case class TransferCompletedEvent(transactionId: Long,
                                  timestamp: String,
                                  displayType: String,
                                  accountId: Option[UUID],
                                  partnerAccountId: Option[UUID],
                                  amount: BigDecimal) {
  def isDisplayable: Boolean  = !displayType.equalsIgnoreCase("NonDisplay")
}

object TransferCompletedEventHandler {
  def props(m: Message, t: TextService) = Props(new TransferCompletedEventHandler(m, t))
}

class TransferCompletedEventHandler(msg: Message,
                              textService: TextService,
                             ) extends Actor with ActorLogging {

  private val event = msg.event.asInstanceOf[TransferCompletedEvent]

  event match {
    case TransferCompletedEvent(_, _, _, _, _, _) ⇒
      sendText("This is the transfer completed message.")
    case _ ⇒ Future.successful(msg.ack())
  }

  private def sendText(text: String) = {
    textService.sendMessage(text)
  }

  def receive: Receive = { case _ ⇒ }
}
