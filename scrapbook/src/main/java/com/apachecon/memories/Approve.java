package com.apachecon.memories;

import com.apachecon.memories.service.ImageService;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

@AuthorizeInstantiation("admin")
public class Approve extends ScrapbookPage {
    private static final long serialVersionUID = 1L;

    public Approve() {
        ScrapbookApplication application = (ScrapbookApplication)getApplication();
        // ApproveService service = application.getApprovalService();
        ImageService service = application.getImageService();

        add(new Gallery("gallery", service.getAll()));
    }
}
