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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.util.ExchangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CheckinProcessor implements Processor {
	private static final Logger LOG = LoggerFactory.getLogger(CheckinProcessor.class);
	
	private ExecutorService executor;
	private Expression destination;
	private Expression tag;
	private Expression value;

	public CheckinProcessor attach(Expression tag) {
		this.tag = tag;
		return this;
	}
	public CheckinProcessor at(Expression destination) {
		this.destination = destination;
		return this;
	}

	public CheckinProcessor keep(Expression value) {
		this.value = value;
		return this;
	}

	public CheckinProcessor ttl(long ttl) {
		// ignore ttl
		return this;
	}

	public void process(Exchange exchange) throws Exception {
		if (executor == null) {
		    executor = exchange.getContext().getExecutorServiceManager().newFixedThreadPool(this, "CLAIM-CHECK", 4);
		}

		String id = tag.evaluate(exchange, String.class);
		exchange.setProperty(ClaimCheck.CLAIMCHECK_TAG_HEADER, id);
		
		final Producer conveyor = findDestination(exchange).createProducer();
		final Exchange baggage = ExchangeHelper.createCorrelatedCopy(exchange, false);

		LOG.info("Checking in 'baggage'");
		executor.submit(new Callable<Exchange>() {
            public Exchange call() throws Exception {
                try {
                    LOG.debug("[claimcheck] {} {}", conveyor, baggage);
                    conveyor.process(baggage);
                } catch (Throwable e) {
                    LOG.warn("Error occurred during processing " + baggage + " checked in at " + conveyor, e);
                }
                return baggage;
            };
        });

		LOG.info("Continue normal flow without 'baggage'");
		Message out = exchange.getOut();
		out.setBody(value.evaluate(exchange, Object.class));
	}

	private Endpoint findDestination(Exchange exchange) {
		return ClaimCheck.endpointLookup(destination).evaluate(exchange, Endpoint.class);
	}
}
