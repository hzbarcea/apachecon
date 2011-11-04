package com.apachecon.memories;

import com.apachecon.memories.service.UserFile;
import com.apachecon.memories.util.Partition;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public class Gallery extends Panel {

    public Gallery(String id, IModel<List<UserFile>> model) {
        super(id);

        RepeatingView frames = new RepeatingView("frames");

        List<List<UserFile>> partitions = Partition.partition(model.getObject(), 12);
        int page = 0;

        for (List<UserFile> files : partitions) {
            WebMarkupContainer container = new WebMarkupContainer(frames.newChildId());
            frames.add(container);

            RepeatingView items = new RepeatingView("items");
            container.add(items);

            for (UserFile file : files) {
                WebMarkupContainer secondContainer = new WebMarkupContainer(items.newChildId());
                items.add(secondContainer);

                secondContainer.add(file.createBigThumb("thumb"));

                enrich(secondContainer, file, page);
            }
            page++;
        }

        add(frames);
    }

    protected void enrich(WebMarkupContainer secondContainer, UserFile file, int page) {
        
    }

}
