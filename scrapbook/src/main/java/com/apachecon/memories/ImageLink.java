package com.apachecon.memories;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

public class ImageLink extends Link<Void> {

    private final File file;

    public ImageLink(String id, File file) {
        super(id);
        this.file = file;
    }

    public void onClick() {
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            @Override
            public void respond(IRequestCycle requestCycle) {
                WebResponse response = (WebResponse)requestCycle.getResponse();
                String name = file.getName();
                response.setContentType("image/" + name.substring(name.lastIndexOf('.') + 1));

                try {
                    InputStream is = new FileInputStream(file);

                    byte[] buffer = new byte[512];
                    int length = 0;
                    while ((length = is.read(buffer)) > 0) {
                        response.write(buffer, 0, length);
                    }

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
