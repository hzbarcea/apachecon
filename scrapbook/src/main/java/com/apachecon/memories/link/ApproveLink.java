package com.apachecon.memories.link;

import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.service.UserFile;

public class ApproveLink extends AbstractLink {
	private static final long serialVersionUID = 1L;

	public ApproveLink(String id, UserFile model) {
        super(id, model);
    }

    @Override
    public void call(ImageService service) {
        service.approve(getModelObject().getName());
    }

}
