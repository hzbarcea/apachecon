package com.apachecon.memories.link;

import com.apachecon.memories.approve.ApproveRequest;
import com.apachecon.memories.approve.ApproveService;
import com.apachecon.memories.service.UserFile;

public class ApproveLink extends AbstractLink {

    public ApproveLink(String id, UserFile model) {
        super(id, model);
    }

    @Override
    public void call(ApproveService service) {
        ApproveRequest message = new ApproveRequest();
        message.setFileName(getModelObject().getName());
        service.approve(message);
    }

}
