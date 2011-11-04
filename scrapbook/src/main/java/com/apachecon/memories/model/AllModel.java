package com.apachecon.memories.model;

import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.service.UserFile;

import java.util.List;

public class AllModel extends AbstractModel {

    @Override
    protected List<UserFile> load() {
        return getService().getAll();
    }

}
