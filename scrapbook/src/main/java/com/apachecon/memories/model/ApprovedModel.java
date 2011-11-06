package com.apachecon.memories.model;

import com.apachecon.memories.service.UserFile;

import java.util.List;

public class ApprovedModel extends AbstractModel {
	private static final long serialVersionUID = 1L;

	@Override
    protected List<UserFile> load() {
        return getService().getApproved();
    }
}
