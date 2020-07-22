package ru.github.sdiver

import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

class SimpleHandler extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]{
  override def handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = {
    new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello")
  }
}
