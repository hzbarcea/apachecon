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
package org.example.activemq.disco.transport;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApacheconTransportFactory extends TcpTransportFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ApacheconTransportFactory.class);
    
    protected Transport createTransport(URI location, WireFormat wf) throws UnknownHostException, IOException {
    	final String hardcoded = "tcp://localhost:62616";
        try {
        	LOG.info("Resolving broker address '{}' to '{}'.", location.toString(), hardcoded);
			return super.createTransport(new URI(hardcoded), wf);
		} catch (URISyntaxException e) {
			throw new UnknownHostException("Cannot create transport. Reason: " + e.getMessage());
		}
    }

}
