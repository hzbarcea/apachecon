package com.apachecon.memories.link;

import com.apachecon.memories.approve.ApproveService;
import com.apachecon.memories.approve.DeclineRequest;
import com.apachecon.memories.service.UserFile;

public class DeclineLink extends AbstractLink {

    public DeclineLink(String id, UserFile model) {
        super(id, model);
    }

    @Override
    public void call(ApproveService service) {
        DeclineRequest message = new DeclineRequest();
        message.setFileName(getModelObject().getName());
        service.decline(message);
    }

}
