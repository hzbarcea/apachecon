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
package com.apachecon.camel.filesplit;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;

import com.apachecon.camel.filesplit.BlockInputStream;
import com.apachecon.camel.filesplit.FileSplitter;

public class FileSplitRouteBuilder extends RouteBuilder {
	private static final int BLOCK_SIZE = 512;
	private static final int READER_COUNT = 4;
	private static final int WORKER_COUNT = 8;
	private static final String READER_URI = "seda:reader";
	private static final String WORKER_URI = "seda:worker";

    @Override
    public void configure() throws Exception {
    	getContext().setTracing(false);

        from("{{demo.source}}").routeId("fetch").autoStartup(false)
            .split(FileSplitter.blocks(BLOCK_SIZE)).parallelProcessing()
                .to(READER_URI);

    	from(READER_URI + "?concurrentConsumers=" + READER_COUNT)
		    .convertBodyTo(InputStream.class)
		    // feel free to comment out following line to reduce noise in the log
		    .to("log:org.example.camel.READER?showBody=false&showExchangePattern=false")
	        .split(perBlock(body().tokenize("\n")))
	            .to(WORKER_URI);

    	from(WORKER_URI + "?concurrentConsumers=" + WORKER_COUNT)
		    // feel free to comment out following line to reduce noise in the log
		    .to("log:org.example.camel.WORKER?showBodyType=false&showExchangePattern=false")
		    .processRef("splitCounter");
    }

    private static Expression perBlock(final Expression inner) {
    	return new Expression() {
            @SuppressWarnings({ "unchecked", "resource" })
			public <T> T evaluate(Exchange exchange, Class<T> type) {
                T result = inner.evaluate(exchange, type);
                Object input = exchange.getIn().getBody();
                if (result instanceof Scanner && input instanceof BlockInputStream) {
                	BlockInputStream body = (BlockInputStream)input;
                	Scanner s = (Scanner)result;
                	result = (T)new BlockSplitIterator(s, body.getBlockSize(), body.getIndex() > 0);
                }
                return result;
            }
    	};
    }

    public static class BlockSplitIterator implements Iterator<String> {
    	Scanner scanner;
    	boolean skip;
    	long blockSize;
    	long count;

    	public BlockSplitIterator(Scanner scanner, long blockSize, boolean skipFirst) {
    		this.scanner = scanner;
    		this.skip = skipFirst;
    		this.blockSize = blockSize;
    		count = 0;
    	}

    	@Override
		public boolean hasNext() {
    		if (count > blockSize) {
    			return false;
    		}
    		boolean result = scanner.hasNext();
    		if (result && skip) {
    			skip = false;
    			next();
    			result = hasNext();
    		}
			return result;
		}

    	@Override
		public String next() {
			String result = scanner.next();
			count += result.length() + 1;
			return result;
		}

    	@Override
		public void remove() {
			// this may break the whole logic
			scanner.remove();
		}
    }
    
    
}
