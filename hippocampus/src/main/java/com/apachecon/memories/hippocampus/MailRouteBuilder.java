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

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JavaUuidGenerator;
import org.apache.camel.spi.UuidGenerator;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailRouteBuilder extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(MailRouteBuilder.class);
    private static final UuidGenerator UUID_GEN = new JavaUuidGenerator();

    @Override
    public void configure() throws Exception {
        from("{{mail.inbox}}").routeId("mail-feed").noAutoStartup()
            .split(attachments()).filter(images()).setHeader(Exchange.FILE_PARENT)
            .simple("${properties:memories-prod.properties:deploy.outbox}")
            .setHeader(Exchange.FILE_NAME, randomName()).to("{{mail.target}}");
    }

    private static Expression randomName() {
        return new Expression() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)UUID_GEN.generateUuid();
            }

            @Override
            public String toString() {
                return "randomName()";
            }
        };
    }

    private static Expression attachments() {
        return new Expression() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                if (exchange.getIn().getAttachments().size() <= 0) {
                    String id = exchange.getIn().getHeader("Message-ID").toString();
                    LOG.info("{}: Dropping message with no attachments (Message-ID: {})", this.toString(), id);
                }
                return (T)exchange.getIn().getAttachments().values();
            }

            @Override
            public String toString() {
                return "attachments()";
            }
        };
    }

    private static Predicate images() {
        return new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                // Check for supported Content-Type
                DataHandler dh = exchange.getIn().getBody(DataHandler.class);
                ObjectHelper.notNull(dh, "DataHandler");
                String ct = dh.getContentType();
                boolean match = ImageHandler.isImage(ct);
                if (!match) {
                    String id = exchange.getIn().getHeader("Message-ID").toString();
                    LOG.info("Dropping attachment of unsupported Content-Type: '{}' (Message-ID: {})", ct, id);
                }
                return match;
            }

            @Override
            public String toString() {
                return "images()";
            }
        };
    }
}
