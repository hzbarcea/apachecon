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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.apachecon.camel.filesplit.SplitCounterProcessor;

public class FileSplitTest extends CamelSpringTestSupport{
    @Override
    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Test
    public void testFileSplitParallelProc() throws Exception {    	
    	PropertiesComponent props = context.getRegistry().lookup("properties", PropertiesComponent.class);
    	int count = Integer.parseInt(props.parseUri("{{demo.message.count}}"));
    	CountDownLatch trigger = new CountDownLatch(count);

    	SplitCounterProcessor splitCounter = context.getRegistry().lookup("splitCounter", SplitCounterProcessor.class);
    	splitCounter.setCounter(trigger);
    	
    	// file poller starts automatically when the route starts
    	// since we created the 'fetch' route with autoStartup=false
    	// polling won't start until we start the route
    	log.info("Expecting to process {} messages", count);
    	context.startRoute("fetch");

    	// set a timeout larger than the expected processing time
    	int timeout = 10 * 1000;
    	boolean success = trigger.await(timeout, TimeUnit.MILLISECONDS);
    	long delta = success ? System.currentTimeMillis() - splitCounter.getTimeStarted() : timeout;
    	String outcome = success ? "finished in" : "timed out after";
    	log.info("Processing {} {} millis", outcome, delta);
    	
    	assertTrue(success);
    }
}
