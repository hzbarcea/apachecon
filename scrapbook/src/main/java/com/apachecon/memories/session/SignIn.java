package com.apachecon.memories.session;

import com.apachecon.memories.ScrapbookPage;
import org.apache.wicket.authroles.authentication.panel.SignInPanel;

public class SignIn extends ScrapbookPage {

    public SignIn() {
        add(new SignInPanel("signIn", true));
    }

}
