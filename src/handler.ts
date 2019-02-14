import { APIGatewayProxyEvent, APIGatewayProxyResult } from 'aws-lambda'

export const handler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
  return {
    statusCode: 200,
    body: 'Connected.'
  }
}

// https://github.com/aws-samples/simple-websockets-chat-app/blob/master/onconnect/app.js
