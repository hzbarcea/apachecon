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
package com.apachecon.memories.link;

import java.io.InputStream;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

import com.apachecon.memories.service.UserFile;

public class ImageLink extends Link<UserFile> {
	private static final long serialVersionUID = 1L;

	public ImageLink(String id, UserFile model) {
        super(id, Model.of(model));
    }

    public void onClick() {
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            @Override
            public void respond(IRequestCycle requestCycle) {
                WebResponse response = (WebResponse)requestCycle.getResponse();
                response.setContentType(getModelObject().getContentType());

                try {
                    InputStream is = getModelObject().getInputStream();

                    byte[] buffer = new byte[512];
                    int length = 0;
                    while ((length = is.read(buffer)) > 0) {
                        response.write(buffer, 0, length);
                    }
                    response.flush();

                    is.close();
                } catch (Exception e) {
                    response.flush();
                }
            }

            @Override
            public void detach(IRequestCycle requestCycle) {
                // TODO Auto-generated method stub

            }
        });
    }
}
