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
package com.apachecon.camel.expedium;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.spi.DataFormat;

import com.apachecon.camel.claimcheck.MessageStore;


public class FileMessageStore implements MessageStore {
	private Map<String, Exchange> store = new ConcurrentHashMap<String, Exchange>();
	private File location;
	private DataFormat df;
	private Expression reader;
	private Endpoint endpoint;
	
	public FileMessageStore(File location, DataFormat df, CamelContext context, Expression reader) {
		this.location = location;
		this.df = df;
		this.reader = reader;
		endpoint = context.getEndpoint("file:" + location.getAbsolutePath());
		
		refresh();
	}

	public void refresh() {
		store.clear();
		for (File msg : location.listFiles()) {
			if (msg.isFile()) {
				Exchange exchange = fromStorage(msg);
				store.put(reader.evaluate(exchange, String.class), exchange);
			}
		}
	}

	@Override
	public void clear() {
		clearStorage();
		store.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return store.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return store.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Exchange>> entrySet() {
		return store.entrySet();
	}

	@Override
	public Exchange get(Object key) {
		return store.get(key);
	}

	@Override
	public boolean isEmpty() {
		return store.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return store.keySet();
	}

	@Override
	public Exchange put(String key, Exchange value) {
		toStorage(value);
		return store.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Exchange> other) {
		for (Map.Entry<? extends String, ? extends Exchange> entry : other.entrySet()) {
			store.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Exchange remove(Object key) {
		Exchange ex = store.get(key);
		File f = new File(location, ex.getExchangeId());
		f.delete();
		return store.remove(key);
	}

	@Override
	public int size() {
		return store.size();
	}

	@Override
	public Collection<Exchange> values() {
		return store.values();
	}
	
	private void clearStorage() {
		for (File msg : location.listFiles()) {
			if (msg.isFile()) {
				msg.delete();
			}
		}
	}
	
	private void toStorage(Exchange exchange) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			df.marshal(exchange, exchange.getIn().getBody(), stream);
			stream.writeTo(new FileOutputStream(new File(location, exchange.getExchangeId())));
		} catch (Exception e) {
			// ignore
		}
	}

	private Exchange fromStorage(File message) {
		Exchange exchange = null;
		try {
			exchange = endpoint.createExchange();
			exchange.setExchangeId(message.getName());
			exchange.getIn().setBody(df.unmarshal(exchange, new FileInputStream(message)));
			
		} catch (Exception e) {
			// ignore
		}
		return exchange;
	}

}
