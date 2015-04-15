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
package org.example.activemq.disco;


import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class CustomDiscoveryTest {
    private static BrokerService BROKER = null;


    @BeforeClass
    public static void startBroker() throws Exception {
        createNewBroker();
    }

    @AfterClass
    public static void stopBroker() throws Exception {
    	if (BROKER != null) {
    		BROKER.stop();
        }
    }

    @Test
    public void testCustomDiscovery() throws Exception {
        // TODO: test custom discovery
    }

    public static void createNewBroker() throws Exception {
    	if (BROKER == null) {
        	BROKER = BrokerFactory.createBroker("broker:(tcp://localhost:61616)/BROKER?persistent=false&useJmx=true&deleteAllMessagesOnStartup=true");
        	BROKER.start();
    	}
    }

}
