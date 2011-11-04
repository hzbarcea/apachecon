package com.apachecon.memories;

import com.apachecon.memories.service.DefaultImageService;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.session.Logout;
import com.apachecon.memories.session.MemoriesWebSession;
import com.apachecon.memories.session.SignIn;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.Session;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.BufferedResponseMapper;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.apachecon.memories.Start#main(String[])
 */
public class ScrapbookApplication extends AuthenticatedWebApplication {

    private static DefaultImageService imageService;

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
        super.init();

        mountPage("/index.html", Index.class);
        mountPage("/signin.html", SignIn.class);
        mountPage("/logout.html", Logout.class);
        mountPage("/browse.html", Browse.class);
        mountPage("/aprove.html", Approve.class);
        mountPage("/upload.html", Upload.class);

        mount(new BufferedResponseMapper() {
            protected String getSessionId() {
                return Session.get().getId();
            }
        });

        // disable cookie with user/pass, it's not safe
        getSecuritySettings().setAuthenticationStrategy(new NoOpAuthenticationStrategy());

        final Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/deploy.properties"));
        } catch (IOException e) {

        }
        imageService = new DefaultImageService();
        imageService.setUploadDirectory(new File(props.getProperty("upload")));
        imageService.setApproveDirectory(new File(props.getProperty("approve")));
        imageService.setDeclineDirectory(new File(props.getProperty("decline")));
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignIn.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return MemoriesWebSession.class;
    }

    public static ImageService getImageService() {
        return imageService;
    }
}
