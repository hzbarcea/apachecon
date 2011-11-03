package com.apachecon.memories;

import com.apachecon.memories.service.ImageService;

import java.io.File;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;


public class Index extends ScrapbookPage {
    private static final long serialVersionUID = -7352468606038233037L;

    public Index() {
        ImageService service = ((ScrapbookApplication) getApplication()).getImageService();

        List<File> approve = service.getAproved();

        add(new Thumbs("thumbs", 6, 3, approve));
    }

}
