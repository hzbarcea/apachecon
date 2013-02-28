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

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apachecon.camel.claimcheck.ClaimCheck.BayInfo;


public class CheckoutProcessor implements Processor {
	private static final Logger LOG = LoggerFactory.getLogger(CheckoutProcessor.class);
	
	private String store;
	private AggregationStrategy strategy;
	private Predicate test;
	private BayInfo bayInfo;
	private boolean carousel = false;

	public CheckoutProcessor bay(String store) {
		this.store = store;
		return this;
	}

	public CheckoutProcessor aggregate(AggregationStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	public CheckoutProcessor check(Predicate test) {
		this.test = test;
		return this;
	}

	public CheckoutProcessor proceed(String exit) {
		bayInfo = ClaimCheck.createBayInfoInMemory(store, exit, strategy, test);
		ClaimCheck.useBay(bayInfo);
		return this;
	}

	public CheckoutProcessor unload() {
		// unload() and proceed() are mutually exclusive
		carousel = true;
		bayInfo = ClaimCheck.getBay(store);
		return this;
	}

	public void process(Exchange exchange) throws Exception {
		MessageStore waiting = carousel ? bayInfo.main : bayInfo.carousel;
		String tag = exchange.getProperty(ClaimCheck.CLAIMCHECK_TAG_HEADER, String.class);
		LOG.info("Processing message with tag '{}'", tag);
		
		if (waiting.containsKey(tag)) {
			// there may be more, but let's ignore this for now
			// Let's aggregate the two exchanges, but order matters!
			// carousel Exchange should be 2nd arg
			Exchange other = waiting.get(tag);
			waiting.remove(tag);
			Exchange resolved = carousel 
			    ? bayInfo.strategy.aggregate(other, exchange)
				: bayInfo.strategy.aggregate(exchange, other);
			
			// check belongings, anything left to do?
			if (bayInfo.check != null && !bayInfo.check.matches(resolved)) {
				// not done, wait for more 'baggage'
				waiting.put(tag, resolved);
				return;
			}
			// proceed to exit
			LOG.info("Corresponding exchange for tag '{}' arrived. Send to destination", tag);
			Producer exit = bayInfo.getEndpoint(exchange.getContext()).createProducer();
			exit.process(resolved);
		} else {
			LOG.info("Corresponding exchange for tag '{}' not yet arrived. Send exchange to message store", tag);
			MessageStore keep = carousel ? bayInfo.carousel : bayInfo.main;
			keep.put(tag, exchange);
		}

	}

}
