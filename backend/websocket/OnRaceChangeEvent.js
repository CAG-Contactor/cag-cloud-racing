const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient({ apiVersion: '2012-08-10', region: process.env.AWS_REGION });


const { WEB_SOCKET_CONNECTIONS_TABLE_NAME, RACES_TABLE_NAME, WEBSOCKET } = process.env;

exports.handler = async (event,context) => {
  let connectionData;
  console.log("Event::: ", JSON.stringify(event));
  try {
    connectionData = await ddb.scan({ TableName: WEB_SOCKET_CONNECTIONS_TABLE_NAME, ProjectionExpression: 'connectionId' }).promise();
    console.log("ConnectionData: ", connectionData.Count);
  } catch (e) {
    console.log("Error:", JSON.stringify(e))
    return { statusCode: 500, body: e.stack };
  }

  const apigwManagementApi = new AWS.ApiGatewayManagementApi({
    apiVersion: '2018-11-29',
    endpoint: event.requestContext.domainName + '/' + event.requestContext.stage
  });

  console.log("apigwManagementApi: ", JSON.stringify(apigwManagementApi.apiVersions));
  // TODO read Races table data
  // Setup event listener (skall göras någon annanstans, kolla https://docs.aws.amazon.com/lambda/latest/dg/with-ddb-example.html)
  // Nedanståend loop skall posta race status
  const postCalls = connectionData.Items.map(async ({ connectionId }) => {
    try {
      console.log("Post to: ", connectionId, JSON.stringify(event));
      await apigwManagementApi.postToConnection({ ConnectionId: connectionId, Data: event }).promise();
    } catch (e) {
      if (e.statusCode === 410) {
        console.log(`Found stale connection, deleting ${connectionId}`);
        await ddb.delete({ TableName: WEB_SOCKET_CONNECTIONS_TABLE_NAME, Key: { connectionId } }).promise();
      } else {
        throw e;
      }
    }
  });

  try {
    await Promise.all(postCalls);
  } catch (e) {
    return { statusCode: 500, body: e.stack };
  }

  return { statusCode: 200, body: 'Data sent.' };
};
