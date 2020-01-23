const AWS = require('aws-sdk');

const { WEB_SOCKET_CONNECTIONS_TABLE_NAME, WEB_SOCKET, STAGE_NAME } = process.env;

// Client for the DynamoDB
const ddb = new AWS.DynamoDB.DocumentClient({
  apiVersion: '2012-08-10',
  region: process.env.AWS_REGION
});

// Client for the Web socket API Gateway
const apigwManagementApi = new AWS.ApiGatewayManagementApi({
  apiVersion: '2018-11-29',
  endpoint: `${WEB_SOCKET}.execute-api.eu-central-1.amazonaws.com/${STAGE_NAME}`
});

/**
 * This handler handles changes in the races table and expects to get the
 * old and new image of a changed/new row
 *
 * A races row contains the following attributes
 * - id
 * - createTime
 * - raceStatus
 * - userName
 * - startTime
 * - splitTime
 * - finishhTime
 *
 * The following is an example of an image (same structure for old and new):
 * <pre>
 *  {
 *     "id":{
 *       "S":"e0649bcf-ad0a-4ec2-952c-fec58c678641"
 *     },
 *     "createTime":{
 *      "N":"1579025089313"
 *     },
 *     "userName":{
 *       "S":"banan"
 *     },
 *     "raceStatus":{
 *       "S":"ARMED"
 *     },
 *     "startTime":{
 *       "N":"1234567890"
 *     },
 *     "splitTime":{
 *       "N":"1579025089313"
 *     },
 *     "finishTime":{
 *       "S":"999999999"
 *     }
 *  }
 * </pre>
 *
 * @param event an event describing the change in the races table
 */
exports.handler = async (event) => {
  let connectionData;
  console.log('Event: ', WEB_SOCKET, STAGE_NAME, JSON.stringify(event))

  // Get all existing connections to the web socket (e.g. the races-ui client)
  try {
    connectionData = await ddb.scan({ TableName: WEB_SOCKET_CONNECTIONS_TABLE_NAME, ProjectionExpression: 'connectionId' }).promise();
    console.log("ConnectionData: ", connectionData.Count);
  } catch (e) {
    console.log("Error:", JSON.stringify(e))
    return { statusCode: 500, body: e.stack };
  }

  // Iterate over all connections and send (i.e broadcast) a message containing the change
  const postCalls = connectionData.Items.map(async ({ connectionId }) => {
    try {
      console.log("Post to: ", connectionId, JSON.stringify(event));
      // We assume there will always be only one row that has changed
      const newImage = event.Records && event.Records[0] && event.Records[0].dynamodb && event.Records[0].dynamodb.NewImage
      const oldImage = event.Records && event.Records[0] && event.Records[0].dynamodb && event.Records[0].dynamodb.OldImage
      await apigwManagementApi.postToConnection({ ConnectionId: connectionId, Data: JSON.stringify({newImage,oldImage}) }).promise();
    } catch (e) {
      console.log('Error in post:', JSON.stringify(e));

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
    console.log('Error in promise:', JSON.stringify(e));
    return { statusCode: 500, body: e.stack };
  }

  return { statusCode: 200, body: 'Data sent.' };
};

