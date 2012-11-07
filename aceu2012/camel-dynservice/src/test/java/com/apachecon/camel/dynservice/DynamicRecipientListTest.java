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
package com.apachecon.camel.dynservice;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicRecipientListTest extends CamelTestSupport {
	private static final Logger LOG = LoggerFactory.getLogger(DynamicRecipientListTest.class);
	private static final String ACON_SERVICE = "ApacheConService";
	private static final String ACON_RESOLVE = "ApacheConResolve";
	private static final String ACON_HI = "Hello ApacheCon";
	private static final String ACON_BYE = "Goodbye ApacheCon";

	@Test
    public void testSimpleInvoke() throws Exception {
        assertInvocationSuccessful("direct:simple", ACON_SERVICE, "direct:apachecon");
	}

	@Test
    public void testHardcodedInvoke() throws Exception {
        assertInvocationSuccessful("direct:hardcoded", ACON_SERVICE, "");
	}

	@Test
    public void testResolvedInvoke() throws Exception {
        assertInvocationSuccessful("direct:resolve", ACON_RESOLVE, "direct:resolve-service");
	}

    private void assertInvocationSuccessful(String uri, String header, String value) throws Exception {
		getMockEndpoint("mock:apachecon").expectedBodiesReceived(ACON_BYE);
		assertEquals(ACON_BYE, template.requestBodyAndHeader(uri, ACON_HI, header, value));
		assertMockEndpointsSatisfied();
    }

	@Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:simple")
                    .recipientList(header(ACON_SERVICE));

                from("direct:hardcoded")
                    .recipientList(hardcoded());
                
                from("direct:resolve")
                    .recipientList(route(header(ACON_RESOLVE)));


                from("direct:resolve-service")
	                .setBody(constant("direct:apachecon"));

                
                from("direct:apachecon")
                    .to("log:com.apachecon.camel.BEFORE")
                    .setBody(constant(ACON_BYE))
                    .to("log:com.apachecon.camel.AFTER")
                    .to("mock:apachecon");
            }
        };
    }

	public static Expression hardcoded() {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				return (T)"direct:apachecon";
			}
		};
	}

	public static Expression route(final Expression eval) {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				String uri = eval.evaluate(exchange, String.class);
				ProducerTemplate t = exchange.getContext().createProducerTemplate();
				LOG.info("Dynamic endpoint resolution via: {}", uri);
				return (T)t.requestBody(uri, "request");
			}
		};
	}

}
