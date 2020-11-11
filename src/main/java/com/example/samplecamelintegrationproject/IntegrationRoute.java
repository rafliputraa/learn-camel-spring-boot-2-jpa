package com.example.samplecamelintegrationproject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IntegrationRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        /*from("timer:myTimer?period=1000")
                .routeId("my integration route")
                .setBody(simple("Hello world at ${header.firedTime}"))
                .log("stream:out");*/
        /*from("timer:myTimer?period=12000")
                .log("RunningSQL query at ${header.firedTime}")
                .setBody(simple("select * from person"))
                .to("jdbc:dataSource")
                .split().simple("${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Map<String, Object> row = exchange.getIn().getBody(Map.class);
//                        System.out.println("Processing: " + row);
                        String firstName = row.get("FIRSTNAME").toString();
                        System.out.println("First Name: " + firstName);
                    }
                })
                .to("mock:result");*/

        // Using Camel to access REST API
        from("timer:myTimer?period=12000")
                .log("Job started")
                .to("direct:httpRoute");
        from("direct:httpRoute")
                .log("Http route Started")
                .setHeader(Exchange.HTTP_METHOD).constant(HttpMethod.GET)
                .to("https://api.bittrex.com/api/v1.1/public/getcurrencies")
                .log("JSON Response: ${body}");

    }
}
