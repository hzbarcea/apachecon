package com.apachecon.memories;

import java.io.File;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class Gallery extends Panel {

    public Gallery(String id, List<File> model) {
        super(id);

        RepeatingView frames = new RepeatingView("frame");
        int itemCount = 0;

        GalleryFrame container = new GalleryFrame(frames.newChildId());

        for (File file : model) {
            if ((++itemCount % 12) == 0) {
                container = new GalleryFrame(frames.newChildId());
                frames.add(container);
            }

            container.addItem(file, itemCount % 4 == 0);
        }

        add(frames);
    }

}
