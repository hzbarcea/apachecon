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
package org.example.activemq.itest;


import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DiscoTest {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoTest.class);
    private static final ArrayList<BrokerService> BROKERS = new ArrayList<BrokerService>();
    
    private static final String DEFAULT_QUEUE = "Stocks";


    @BeforeClass
    public static void startBroker() throws Exception {
        createBroker("broker2");
    }

    @AfterClass
    public static void stopBroker() throws Exception {
        for (BrokerService b : BROKERS) {
            if (b != null) {
                b.stop();
            }
        }
    }

    @Test
    public void testDiscoConnection() throws Exception {
        Assert.assertEquals(1, BROKERS.size());

        ConnectionFactory factory =  new ActiveMQConnectionFactory("apachecon:welcome-to:Austin");
        Connection consumerConnection = factory.createConnection();
        consumerConnection.start();
        Connection producerConnection = factory.createConnection();
        producerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = consumerSession.createConsumer(consumerSession.createQueue(DEFAULT_QUEUE));
        MessageProducer producer = producerSession.createProducer(producerSession.createQueue(DEFAULT_QUEUE));

        final CountDownLatch messageSignal = new CountDownLatch(1);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(javax.jms.Message message) {
                try {
                    LOG.info("Received message {} with content: \"{}\".", 
                        message.getJMSMessageID().toString(), ((ActiveMQTextMessage)message).getText());
                    messageSignal.countDown();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread.sleep(200);
        producer.send(producerSession.createTextMessage("QQQ: $1000.00"));
        messageSignal.await(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(0, messageSignal.getCount());

        producerConnection.stop();
        consumerConnection.stop();
    }

    public static BrokerService createBroker(String name) throws Exception {
        BrokerService b = BrokerFactory.createBroker("xbean:META-INF/org/apache/activemq/" + name + ".xml");
        if (!name.equals(b.getBrokerName())) {
            LOG.warn("Broker name mismatch (expecting '{}'). Check configuration.", name);
            return null;
        }
        BROKERS.add(b);
        b.start();
        b.waitUntilStarted();
        LOG.info("Broker '{}' started.", name);
        return b;
    }

}
