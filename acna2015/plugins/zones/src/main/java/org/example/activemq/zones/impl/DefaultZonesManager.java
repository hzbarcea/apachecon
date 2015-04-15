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
package org.example.activemq.zones.impl;

import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.security.SecurityContext;
import org.example.activemq.zones.ZonesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultZonesManager implements ZonesManager {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultZonesManager.class);

	@Override
	public String zoneDestination(ConnectionContext context, ActiveMQDestination destination) {
		SecurityContext sc = context.getSecurityContext();
		String un = sc != null ? sc.getUserName() : null;
		int zi = un != null ? un.indexOf("@") : -1;
		String zid = zi > 0 ? un.substring(zi + 1) : null;
		return new StringBuffer(ZonesConstants.ZONE_BEGIN)
            .append(zid != null ? zid : ZonesConstants.ZONE_ANONYMOUS)
            .append(ZonesConstants.ZONE_END)
            .append(ActiveMQDestination.PATH_SEPERATOR)
            .append(destination.getPhysicalName())
            .toString();
	}

	@Override
	public String getZoneId(ActiveMQDestination destination) {
		String pn = destination.getPhysicalName();

		if (AdvisorySupport.isAdvisoryTopic(destination)) {
			int p = pn.indexOf(ZonesConstants.ZONE_BEGIN);
			if (p != -1) {
				pn = pn.substring(p);
			}
		}

		// TODO: support for composite destination
		if (pn.startsWith(ZonesConstants.ZONE_BEGIN)) {
		    int p = pn.indexOf(ZonesConstants.ZONE_END, ZonesConstants.ZONE_BEGIN.length());
		    int s = p + ZonesConstants.ZONE_END.length();
		    if (p != -1 && pn.length() > s && pn.indexOf(ActiveMQDestination.PATH_SEPERATOR, s) == s) {
		    	return pn.substring(1, p);
		    }
		    LOG.warn("Invalid physical name for zoned destination '{}'", pn);
        } // else... not a zoned destination 
		return null;
	}

}
