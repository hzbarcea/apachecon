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
package com.apachecon.memories.session;

import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class MemoriesWebSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = 4698773864853366747L;
    private Properties users;
    private Roles roles;

    public MemoriesWebSession(Request request) {
        super(request);

        users = new Properties();
        try {
            users.load(getClass().getResourceAsStream("/users.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load users", e);
        }
    }

    @Override
    public boolean authenticate(String username, String password) {

        if (username.trim().length() == 0 || password.trim().length() == 0) {
            warn("Provide user and password");
            return false;
        }

        if (users.getProperty(username) != null) {
            if (users.getProperty(username).equals(MD5Util.encode(password))) {
                roles = new Roles("admin");
                return true;
            }
            warn("Wrong username or password");
        }
        return false;
    }

    @Override
    public Roles getRoles() {
        return roles;
    }

}
