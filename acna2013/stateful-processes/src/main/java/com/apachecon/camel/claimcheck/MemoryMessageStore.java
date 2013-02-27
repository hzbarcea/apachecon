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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Exchange;


public class MemoryMessageStore implements MessageStore {
	private Map<String, Exchange> store = new ConcurrentHashMap<String, Exchange>();

	@Override
	public void clear() {
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
		return store.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Exchange> other) {
		store.putAll(other);
	}

	@Override
	public Exchange remove(Object key) {
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
	
}
