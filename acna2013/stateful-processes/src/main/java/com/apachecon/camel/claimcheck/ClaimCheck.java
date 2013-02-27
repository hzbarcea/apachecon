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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;


public class ClaimCheck {
	public static final String CLAIMCHECK_TAG_HEADER = "CamelClaimcheckTag";
	private static final AtomicInteger SIMPLE_COUNTER = new AtomicInteger(0);
	private static Map<String, BayInfo> CHECKOUT_BAYS = new ConcurrentHashMap<String, BayInfo>();

    public static CheckinProcessor checkin() {
    	return new CheckinProcessor();
    }

    public static CheckinProcessor ci() {
    	return checkin();
    }

    public static CheckoutProcessor checkout() {
    	return new CheckoutProcessor();
    }

    public static CheckoutProcessor co() {
    	return checkout();
    }

    public static ArrivalRouteBuilder arrival(RouteBuilder builder) {
    	return new ArrivalRouteBuilder(builder);
    }

    public final static class ArrivalRouteBuilder {
    	private final RouteBuilder builder;
    	private String uri;

		public ArrivalRouteBuilder(RouteBuilder builder) {
			this.builder = builder;
		}
		
		public ArrivalRouteBuilder unload(String uri) {
			this.uri = uri;
			return this;
		}
		
		public void bay(String store) {
			// TODO: should check if from == null
			builder.from(uri).process(co().bay(store).unload());
		}
    }

    public final static class BayInfo {
    	public String id;
    	public MessageStore main;
    	public MessageStore carousel;
    	public String uri;
    	public AggregationStrategy strategy;
    	private Endpoint exit;
    	
    	public synchronized Endpoint getEndpoint(CamelContext context) {
    		if (exit == null) {
    			exit = context.getEndpoint(uri);
    		}
    		return exit;
    	}
    }
    
    public static BayInfo createBayInfoInMemory(String bay, String uri, AggregationStrategy strategy) {
    	BayInfo bi = new BayInfo();
    	bi.id = bay;
    	bi.main = new MemoryMessageStore();
    	bi.carousel = new MemoryMessageStore();
    	bi.strategy = strategy;
    	bi.uri = uri;
    	return bi;
    }
    
    public static void useBay(BayInfo bayInfo) {
    	CHECKOUT_BAYS.put(bayInfo.id, bayInfo);
    }

    public static BayInfo getBay(String id) {
    	return CHECKOUT_BAYS.get(id);
    }

    public static Expression tag(final Expression id) {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				return (T)("TAG-" + id.evaluate(exchange, String.class));
			}
		};
    }
    
	public static Expression exchangeId() {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				return (T)((DefaultExchange)exchange).getExchangeId();
			}
		};
	}

	public static Expression simpleCounter() {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				return (T)Integer.toHexString(SIMPLE_COUNTER.incrementAndGet());
			}
		};
	}

	public static Expression endpointLookup(final Expression destination) {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				String uri = destination.evaluate(exchange, String.class);
				return (T)exchange.getContext().getEndpoint(uri);
			}
		};
	}
}
