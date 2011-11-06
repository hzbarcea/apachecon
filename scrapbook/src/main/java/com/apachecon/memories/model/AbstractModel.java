package com.apachecon.memories.model;

import com.apachecon.memories.ScrapbookApplication;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.service.UserFile;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

abstract class AbstractModel extends LoadableDetachableModel<List<UserFile>> {
	private static final long serialVersionUID = 1L;

	public ImageService getService() {
        return ScrapbookApplication.getImageService();
    }
}
