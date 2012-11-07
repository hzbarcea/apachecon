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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BlockInputStream extends BufferedInputStream {
	private final long blockSize;
	private final int index;
	private boolean advance = true;

	public BlockInputStream(InputStream in, long blockSize, int index) {
		super(in);
		this.blockSize = blockSize;
		this.index = index;
	}

	public BlockInputStream(InputStream in, int size, long blockSize, int index) {
		super(in, size);
		this.blockSize = blockSize;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public long getBlockSize() {
		return blockSize;
	}

	@Override
	public synchronized int read() throws IOException {
		step();
		return super.read();
	}

	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		step();
		return super.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		step();
		return super.read(b);
	}

	private synchronized void step() throws IOException {
		if (advance) {
			this.skip(blockSize * index);
			advance = false;
		}
	}

}
