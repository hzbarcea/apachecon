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
package com.apachecon.memories.hippocampus;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;

import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MailConnectionTest extends CamelSpringTestSupport {

    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Test
    public void testMailEndpoint() throws Exception {
        Mailbox.clearAll();

        Endpoint endpoint = context.getEndpoint("{{mbox.smtp}}");
        assertNotNull(endpoint);
        
        Exchange ex1 = endpoint.createExchange();
        Message in = ex1.getIn();
        in.setBody("Hello World");
        in.addAttachment("feather-small.gif", new DataHandler(new FileDataSource("src/test/resources/img/feather-small.gif")));
        in.addAttachment("talend-logo", new DataHandler(new FileDataSource("src/test/resources/img/talend-logo.jpg")));

        Exchange ex2 = endpoint.createExchange();
        ex2.getIn().setBody("Bye World... without attachments");

        Producer producer = endpoint.createProducer();
        producer.start();
        producer.process(ex1);
        producer.process(ex2);
        producer.stop();
        Thread.sleep(5000);

        Endpoint target = context.getEndpoint("{{mail.target}}");
        if (target instanceof MockEndpoint) {
        	MockEndpoint mock = (MockEndpoint)target;
        	
            mock.expectedMessageCount(2);
            mock.assertIsSatisfied();

            assertTrue(mock.assertExchangeReceived(0).getIn().getBody() instanceof DataHandler);
            assertTrue(mock.assertExchangeReceived(1).getIn().getBody() instanceof DataHandler);
        }
    }
}
