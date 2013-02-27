/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apachecon.camel.claimcheck;

import static com.apachecon.camel.claimcheck.ClaimCheck.exchangeId;
import static com.apachecon.camel.claimcheck.ClaimCheck.tag;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;


public class ClaimCheckTest extends CamelTestSupport {
	private static final String DEMO_HEADER = "CamelDemo";

	@Before
	public void setUp() throws Exception {
		super.setUp();
		deleteDirectory("target/acna/checkin");
		deleteDirectory("target/acna/arrival");
	}

	@Test
    public void testSimpleClaimCheck() throws Exception {
		MockEndpoint exit = context.getEndpoint("mock:exit", MockEndpoint.class);
		exit.expectedMessageCount(1);
		exit.expectedBodiesReceived("HELLO WORLD");

		// claimcheck only makes sense for in-only mep
		template.sendBodyAndHeader("direct:simple", "Hello world", DEMO_HEADER, "acna2013");
		
		Thread.sleep(2000);
		context.startRoute("baggage");
		
		assertMockEndpointsSatisfied();
	}

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:simple")
                    .process(ClaimCheck.checkin()
                        .at(constant("direct:checkin"))
                        .attach(tag(exchangeId()))
                        .keep(header(DEMO_HEADER)))
                    .setHeader(Exchange.FILE_NAME, property(ClaimCheck.CLAIMCHECK_TAG_HEADER))
                    .to("file:target/acna/messages")
                    .to("seda:queue");
                
                from("direct:checkin")
                    .setHeader(Exchange.FILE_NAME, property(ClaimCheck.CLAIMCHECK_TAG_HEADER))
                    .to("file:target/acna/checkin");

                from("seda:queue")
                    .process(ClaimCheck.co()
                        .bay("Portland")
                        .aggregate(baggageToUpper())
                        .proceed("direct:exit"));

                from("direct:exit")
                    .to("log:exit")
                    .to("mock:exit");

                from("file:target/acna/arrival")
                    .setProperty(ClaimCheck.CLAIMCHECK_TAG_HEADER, header("Exchange.FILE_NAME"))
                    .to("seda:arrival");

                ClaimCheck
                    .arrival(this)
                    .unload("seda:arrival")
            	    .bay("Portland");
                
                // for test purposes
                from("file:target/acna/checkin").routeId("baggage").autoStartup(false)
                    .to("file:target/acna/arrival");
            }
        };
    }
    
    private AggregationStrategy baggageToUpper() {
    	return new AggregationStrategy() {
            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                if (oldExchange == null || newExchange == null) {
                    throw new RuntimeCamelException("Can only aggregate when claimcheck exchanges are paired");
                }
                String body = newExchange.getIn().getBody(String.class);

                oldExchange.getIn().setBody(body.toUpperCase());
                return oldExchange;
             }
    	};
    }

}
