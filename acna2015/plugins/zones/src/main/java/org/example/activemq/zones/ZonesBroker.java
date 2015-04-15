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
package org.example.activemq.zones;


import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZonesBroker extends BrokerFilter {
	private static final Logger LOG = LoggerFactory.getLogger(ZonesBroker.class);

    private ZonesPlugin zoner;
    
	public ZonesBroker(Broker next, ZonesPlugin zoner) {
		super(next);
		this.zoner = zoner;
	}

	public ZonesBroker(Broker next) {
		this(next, null);
	}

	
	@Override
	public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
		super.send(producerExchange, messageSend);
	}

	@Override
	public Destination addDestination(ConnectionContext context, ActiveMQDestination destination, boolean createIfTemporary) throws Exception {
		if (!AdvisorySupport.isAdvisoryTopic(destination) && zoner.getManager().getZoneId(destination) == null) {
			String pn = zoner.getManager().zoneDestination(context, destination);
			LOG.debug("Zoning destination '{}' to '{}'", destination.getPhysicalName(), pn);
			destination.setPhysicalName(pn);
		}
		return super.addDestination(context, destination, createIfTemporary);
	}

    @Override
	public void removeDestination(ConnectionContext context, ActiveMQDestination destination, long timeout) throws Exception {
		super.removeDestination(context, destination, timeout);
	}

	@Override
	public void addDestinationInfo(ConnectionContext context, DestinationInfo info) throws Exception {
		super.addDestinationInfo(context, info);
	}

	@Override
	public void removeDestinationInfo(ConnectionContext context, DestinationInfo info) throws Exception {
		super.removeDestinationInfo(context, info);
	}


	public ZonesPlugin getZoner() {
		return zoner;
	}

	public void setZoner(ZonesPlugin zoner) {
		this.zoner = zoner;
	}

}
