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
        mountPage("/approve.html", Approve.class);
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
        File rootDirectory = new File(props.getProperty("data.dir"));
        rootDirectory.mkdirs();
        
        imageService = new DefaultImageService();
        
        imageService.setArchiveDirectory(new File(rootDirectory, "archive"));
        imageService.setUploadDirectory(new File(rootDirectory, "upload"));
        imageService.setApproveDirectory(new File(rootDirectory, "approve"));
        imageService.setDeclineDirectory(new File(rootDirectory, "decline"));
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
