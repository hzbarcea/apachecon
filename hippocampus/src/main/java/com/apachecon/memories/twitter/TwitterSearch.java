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
package com.apachecon.memories.twitter;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.util.ExchangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TwitterSearch {
	private static final Logger LOG = LoggerFactory.getLogger(TwitterSearch.class);

	public List<Tweet> tweets(Search search) {
		return search.getResults();
	}
	
	public static AggregationStrategy defaultAggregation() {
		return new TweetsAggregation();
	}

	public static Foo defaultFoo() {
		return new Foo();
	}

	// Default AggregationStrategy for (poll)Enrich
	private static class TweetsAggregation implements AggregationStrategy {
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        	// ignore request, just use newExchange, the result of Enrich
        	if (oldExchange != null) {
                ExchangeHelper.copyResultsPreservePattern(oldExchange, newExchange);
        	}
        	return oldExchange;
        }
    }
	public static class Foo {
		public void bar(Search search) {
			if (search.getNext_page() != null) {
				LOG.info("FOOBAR: next_page: {}", search.getNext_page());
			}
		}
	}
}
