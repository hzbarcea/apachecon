package com.apachecon.memories;

import java.io.File;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class GalleryFrame extends Panel {

    private RepeatingView items;

    public GalleryFrame(String id) {
        super(id);

        add(items = new RepeatingView("item"));
        items.setRenderBodyOnly(true);
    }

    public void addItem(File file, boolean last) {
        items.add(new GalleryItem(items.newChildId(), file, last));
    }
}
