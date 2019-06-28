let AWS = require('aws-sdk');
const sns = new AWS.SNS();
var dynamo = new AWS.DynamoDB.DocumentClient();

exports.handler = function (event, context, callback) {

  console.log("Event: "+event);
  console.log("Id: "+event.Records[0].messageAttributes.Id.stringValue);
  console.log("Number: "+event.Records[0].messageAttributes.Number.stringValue);
  console.log("Message: "+event.Records[0].messageAttributes.Message.stringValue);
  console.log("\n");
  var id = event.Records[0].messageAttributes.Id.stringValue;
  var number = event.Records[0].messageAttributes.Number.stringValue;
  var message = event.Records[0].messageAttributes.Message.stringValue;

  
    sns.publish({
    	Message: message,
    	MessageAttributes: {
    		'AWS.SNS.SMS.SMSType': {
    		DataType: 'String',
    		StringValue: 'Promotional'
    	},
    	'AWS.SNS.SMS.SenderID': {
    		DataType: 'String',
    		StringValue: 'dwolla'
    	},
    },
    PhoneNumber: number
    }).promise()
    .then(data => {
    	console.log("Sent message to", number);
    	callback(null, data);
    })
    .catch(err => {
    	console.log("Sending failed", err);
    	callback(err);
    });
} 