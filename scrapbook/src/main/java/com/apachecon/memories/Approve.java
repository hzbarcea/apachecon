package com.apachecon.memories;

import com.apachecon.memories.model.AllModel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@AuthorizeInstantiation("admin")
public class Approve extends ScrapbookPage {

    public Approve() {
        add(new ApproveGallery("gallery", new AllModel()));
    }
}
