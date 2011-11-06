package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Upload page - allows users to share their pictures.
 * 
 * @author lukasz
 */
public class Upload extends ScrapbookPage {
	private static final long serialVersionUID = 1L;

	private static final List<String> contentTypes = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif");

    public Upload() {
        add(new FeedbackPanel("feedback"));

        ApprovedModel model = new ApprovedModel();
        add(new Thumbs("thumbs", 12, 4, model));
        add(new BookmarkablePageLink<Browse>("browse", Browse.class));

        add(new UploadForm("uploadForm", contentTypes));
    }
}
