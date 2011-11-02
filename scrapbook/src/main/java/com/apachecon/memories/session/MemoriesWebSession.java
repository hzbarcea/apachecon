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
