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

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.apachecon.camel.expedium.types.Request;
import com.apachecon.camel.expedium.types.Reservation;


public class ReservationProcessTest extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("/META-INF/spring/camel-context.xml");
	}

	@Test
	public void testReservation() throws Exception {
		String id = "acna13-001";
		String name = "Hadrian";

		Reservation reservation = new Reservation();
		reservation.setId(id);
		reservation.setName(name);
		reservation.setOrigin("IAD");
		reservation.setDestination("PDX");
		reservation.setRequest("airline,hotel");

		template.sendBody("direct:reservations", reservation);
		Thread.sleep(1000);

		Request request = new Request();
		request.setId(id);
		request.setName(name);
		request.setType("airline");
		request.setValue("ACME Airlines");
		template.sendBody("file:target/expedium/reply", request);
		Thread.sleep(1000);

		request = new Request();
		request.setId(id);
		request.setName(name);
		request.setType("hotel");
		request.setValue("Hilton");
		template.sendBody("file:target/expedium/reply", request);

		Thread.sleep(5000);
	} 

}
