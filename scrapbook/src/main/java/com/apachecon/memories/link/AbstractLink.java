package com.apachecon.memories.link;

import com.apachecon.memories.ScrapbookApplication;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.service.UserFile;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLink extends AjaxLink<UserFile> {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractLink(String id, UserFile model) {
        super(id, Model.of(model));
    }

    public final void onClick(AjaxRequestTarget target) {
        try {
            call(ScrapbookApplication.getImageService());
        } catch (Exception e) {
            logger.error("Error calling web service", e);
        }
        update(target);
    }

    // for anonymous classes
    protected void update(AjaxRequestTarget target) {
        
    }

    public abstract void call(ImageService service);

}
