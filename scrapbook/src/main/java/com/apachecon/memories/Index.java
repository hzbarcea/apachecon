package com.apachecon.memories;

import com.apachecon.memories.service.ImageService;

import java.io.File;
import java.util.List;

public class Index extends ScrapbookPage {
    private static final long serialVersionUID = -7352468606038233037L;

    public Index() {
        ImageService service = ((ScrapbookApplication)getApplication()).getImageService();

        List<File> approve = service.getAproved();

        add(new Thumbs("thumbs", 6, 3, approve));
    }

}
