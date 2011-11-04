package com.apachecon.memories.session;

import com.apachecon.memories.ScrapbookPage;
import org.apache.wicket.authroles.authentication.panel.SignInPanel;

/**
 * Login page for admins.
 * 
 * @author lukasz
 */
public class SignIn extends ScrapbookPage {

    private static final long serialVersionUID = -3618677566671723851L;

    public SignIn() {
        add(new SignInPanel("signIn", false));
    }

}
