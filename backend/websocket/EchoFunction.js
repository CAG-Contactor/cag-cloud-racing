const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient({apiVersion: '2012-08-10', region: process.env.AWS_REGION});

exports.handler = function (event, context, callback) {
    console.log("Receiving event from database!!");
    console.log("Event: ", JSON.stringify(event));
    console.log("Event: ", JSON.stringify(context));
    callback(null, "done");
};