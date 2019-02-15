import { APIGatewayProxyEvent, APIGatewayProxyResult } from 'aws-lambda'
import moment from 'moment'

export const handler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
  return {
    statusCode: 200,
    body: moment.utc().toISOString()
  }
}
