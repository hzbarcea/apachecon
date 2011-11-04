package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;

public class Browse extends ScrapbookPage {

    public Browse() {
        add(new BrowseGallery("gallery", new ApprovedModel()));
    }
}
