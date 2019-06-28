package com.dwolla.text.alerts.domain

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.{MessageAttributeValue, SendMessageRequest}

class TextService extends {

  import com.amazonaws.auth.BasicAWSCredentials

  def sendMessage(text: String) = {

    val accessKey = "AKIAJARV2SQV6S3VBXXQ"
    val secretKey = "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"

    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-west-2").build()

    val queueUrl = "https://sqs.us-west-2.amazonaws.com/676548967657/dwolla-texter"

    import java.util

    val attributes = new util.HashMap[String, MessageAttributeValue]
    attributes.put("Id", new MessageAttributeValue().withDataType("String").withStringValue("1234-ABCD-5678"))
    attributes.put("Number", new MessageAttributeValue().withDataType("String").withStringValue("+15153719995"))
    attributes.put("Message", new MessageAttributeValue().withDataType("String").withStringValue(text))

    val send_msg_request = new SendMessageRequest()
      .withQueueUrl(queueUrl)
      .withMessageBody("hello world")
      .withMessageAttributes(attributes)
      .withDelaySeconds(5)
    sqs.sendMessage(send_msg_request)
  }
}
