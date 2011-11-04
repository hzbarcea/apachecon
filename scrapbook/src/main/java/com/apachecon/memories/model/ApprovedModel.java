package com.apachecon.memories.model;

import com.apachecon.memories.service.UserFile;

import java.util.List;

public class ApprovedModel extends AbstractModel {

    @Override
    protected List<UserFile> load() {
        return getService().getApproved();
    }

}
