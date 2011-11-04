package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class Index extends ScrapbookPage {

    public Index() {
        add(new Thumbs("thumbs", 6, 3, new ApprovedModel()));
        add(new BookmarkablePageLink("browse", Browse.class));
    }

}
