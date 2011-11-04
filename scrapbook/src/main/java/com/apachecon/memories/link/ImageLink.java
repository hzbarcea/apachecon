package com.apachecon.memories.link;

import com.apachecon.memories.service.UserFile;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

public class ImageLink extends Link<UserFile> {

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
