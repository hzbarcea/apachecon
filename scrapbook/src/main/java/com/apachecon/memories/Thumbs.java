package com.apachecon.memories;

import java.io.File;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class Thumbs extends Panel {

    public Thumbs(String id, int maxElems, int rowElements, List<File> model) {
        super(id);

        RepeatingView repeater = new RepeatingView("items");
        int itemCount = 0;
        for (File file : model) {
            MarkupContainer container = new WebMarkupContainer(repeater.newChildId());
            Link link = new BookmarkablePageLink("link", Upload.class);
            ResourceStreamResource imageResource = new ResourceStreamResource(new FileResourceStream(file));
            link.add(new Image("thumb", imageResource));
            container.add(link);

            repeater.add(container);

            if ((++itemCount % rowElements) == 0) {
                container.add(AttributeModifier.append("class", "last"));
            }

            if (itemCount == maxElems) {
                break;
            }
        }

        add(repeater);
    }


}
