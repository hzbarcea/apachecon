package com.apachecon.memories.model;

import java.util.List;

import com.apachecon.memories.service.UserFile;

public class AllModel extends AbstractModel {
	private static final long serialVersionUID = 1L;

	@Override
    protected List<UserFile> load() {
        return getService().getAll();
    }
}
