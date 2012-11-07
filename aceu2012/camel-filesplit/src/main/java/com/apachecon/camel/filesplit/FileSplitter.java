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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.camel.CamelException;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConverter;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.util.ObjectHelper;

@Converter
public final class FileSplitter {
	public static final String CHUNK_INDEX = "SplitterChunkIndex";
	public static final String SEQUENCE_INDEX = "SplitterSequenceIndex";
	public static final String SEQUENCE_LAST = "SplitterSequenceLast";
	public static final Message EMPTY_MESSAGE = new DefaultMessage();

	public static Expression nullBody() {
		return new Expression()  {
			@Override
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				return (T)null;
			}
	    };
	}

	public static Expression blocks(final long blockSize) {
		return new Expression()  {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				if (type.isAssignableFrom(List.class) && exchange.getIn().getBody() instanceof GenericFile) {
					GenericFile<?> input = (GenericFile<?>)exchange.getIn().getBody();
					long size = input.getFileLength();
					int count = (int)((size + blockSize - 1) / blockSize);
					List<FileBlock> answer = new ArrayList<FileBlock>(count);
					for (int i = 0; i < count; i++) {
						answer.add(new FileBlock(input, blockSize, i));
					}
				    return (T)answer;
				}
				return null;
		    }
		};
	}

	public static Processor delayLine(final int length) {
		return new Processor() {
			private Queue<Message> queue = new LinkedList<Message>();
			private int index = 0;

			@Override
			public void process(Exchange exchange) throws Exception {
				queue.add(exchange.getIn());

				Message out = length < queue.size() ? queue.poll() : EMPTY_MESSAGE;
				if (out.getBody() != null) {
					out.setHeader(SEQUENCE_INDEX, index++);
					Message prev = queue.size() > 0 ? queue.peek() : EMPTY_MESSAGE;
					out.setHeader(SEQUENCE_LAST, prev.getBody() == null);
				} else {
					index = 0;
				}
				exchange.setOut(out);
			}
		};
	}

	public static Predicate notEmpty() { 
	    return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				Integer index = exchange.getIn().getHeader(SEQUENCE_INDEX, Integer.class);
				return exchange.getIn().getBody() != null && index != 0;
			}
	    };
    }

	@Converter
	public static InputStream toInputStream(FileBlock block, Exchange exchange) throws IOException, CamelException {
        if (block.getFile().getFile() instanceof java.io.File) {
        	ObjectHelper.notNull(exchange, "Exchange");
        	exchange.getIn().setHeader(CHUNK_INDEX, block.getIndex());
            return new BlockInputStream(new FileInputStream((File)block.getFile().getFile()), block.getChunk(), block.getIndex());
        }
        return GenericFileConverter.genericFileToInputStream(block.getFile(), exchange);
	}
}
