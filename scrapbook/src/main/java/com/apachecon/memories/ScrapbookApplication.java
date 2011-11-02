package com.apachecon.memories;

import com.apachecon.memories.session.MemoriesWebSession;
import com.apachecon.memories.session.SignIn;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.apachecon.memories.Start#main(String[])
 */
public class ScrapbookApplication extends AuthenticatedWebApplication {

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<Index> getHomePage() {
        return Index.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
//        mountPage("/index.html", Index.class);
//        mountPackage("/signin.html", SignIn.class);
//        mountPackage("/aprove.html", SignIn.class);
//        mountPackage("/upload.html", Upload.class);
        super.init();

    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignIn.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return MemoriesWebSession.class;
    }

}
