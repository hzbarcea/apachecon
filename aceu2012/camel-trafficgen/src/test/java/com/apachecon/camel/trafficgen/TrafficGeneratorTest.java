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
package com.apachecon.camel.trafficgen;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TrafficGeneratorTest extends CamelTestSupport {
	private final int MULTIPLIER = 500;
	private String[] requests = {
		"919",  // Raleigh, NC 
		"202",  // Washington, DC
		"212",  // New York, NY
		"617",  // Boston, MA
		"650"}; // Los Altos, CA

	@Test
    public void testGenerateTestData() throws Exception {
		for (String r: requests) {
			template.sendBody("direct:start", r);
		}
	}

	@Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                    .setHeader("AreaCode").body()
                    .split(copies(distribution(body())))
                    .loop(MULTIPLIER)
                    .setBody().method(GenerateRecord.class)
                    .split(clones())
                    .to("seda:phone");
                
                from("seda:phone")
                    // .to("stream:out");
                    .to("stream:file?fileName=target/traffic.csv");
            }
        };
    }

	public static Expression distribution(final Expression eval) {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				String key = eval.evaluate(exchange, String.class);
				Integer count = key.equals("919") ? 5
				    : key.equals("202") ? 25
				    : key.equals("212") ? 10
				    : key.equals("617") ? 35
				    : key.equals("650") ? 25 : 0;
				return (T)count;
			}
		};
	}

	public static Expression copies(final Expression eval) {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				Integer count = eval.evaluate(exchange, Integer.class);
				if (count != null && type.isAssignableFrom(List.class)) {
					String body = (String)exchange.getIn().getBody();
					List<String> copies = new ArrayList<String>(count);
					for (int i = 0; i < count; i++) {
						copies.add(body);
					}
					return (T)copies;
				}
				return null;
			}
		};
	}

	public static Expression clones() {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				if (!type.isAssignableFrom(List.class)) {
					return null;
				}

				Object body = exchange.getIn().getBody();
	    		int count = (body instanceof RandomCloneable) ? ((RandomCloneable)body).cloneCount() : 1;

	    		List<Object> clones = new ArrayList<Object>(count);
				clones.add(body);
				for (int i = 1; i < count; i++) {
					clones.add((body instanceof RandomCloneable) ? ((RandomCloneable)body).newClone() : body);
				}
				return (T)clones;
			}
		};
	}
	
	public interface RandomCloneable extends Cloneable {
		int cloneCount();
		RandomCloneable newClone();
	}

	public static final class GenerateRecord {
        public Person value(@Header(value="AreaCode") final String code) {
        	return new Person(code);
        }
	}

}
