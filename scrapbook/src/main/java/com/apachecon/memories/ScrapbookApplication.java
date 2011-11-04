package com.apachecon.memories;

import com.apachecon.memories.approve.ApproveService;
import com.apachecon.memories.service.DefaultImageService;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.session.Logout;
import com.apachecon.memories.session.MemoriesWebSession;
import com.apachecon.memories.session.SignIn;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy;
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

    private static DefaultImageService imageService;
    private static ApproveService approveService;

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
        mountPage("/index", Index.class);
        mountPackage("/signin", SignIn.class);
        mountPackage("/logout", Logout.class);
        mountPackage("/approve", Approve.class);
        mountPackage("/upload", Upload.class);
        super.init();

        // disable cookie with user/pass, it's not safe
        getSecuritySettings().setAuthenticationStrategy(new NoOpAuthenticationStrategy());

        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/deploy.properties"));
        } catch (IOException e) {

        }
        imageService = new DefaultImageService();
        imageService.setUploadDirectory(new File(props.getProperty("upload")));
        imageService.setApproveDirectory(new File(props.getProperty("approve")));
        imageService.setDeclineDirectory(new File(props.getProperty("decline")));

        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(ApproveService.class);
        factoryBean.setAddress(props.getProperty("serviceUrl"));

        // we know what we are doing
        factoryBean.setFeatures((List)Arrays.asList(new LoggingFeature()));
        approveService = (ApproveService)factoryBean.create();
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

    public static ApproveService getApprovalService() {
        return approveService;
    }
}
