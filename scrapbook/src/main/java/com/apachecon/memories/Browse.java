package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;

public class Browse extends ScrapbookPage {
	private static final long serialVersionUID = 1L;

	public Browse() {
        add(new BrowseGallery("gallery", new ApprovedModel()));
    }
}
