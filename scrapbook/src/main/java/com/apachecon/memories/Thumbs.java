package com.apachecon.memories;

import com.apachecon.memories.service.UserFile;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class Thumbs extends Panel {

    public Thumbs(String id, int maxElems, int rowElements, IModel<List<UserFile>> model) {
        super(id, model);

        RepeatingView repeater = new RepeatingView("items");
        int itemCount = 0;
        for (UserFile file : model.getObject()) {
            MarkupContainer container = new WebMarkupContainer(repeater.newChildId());
            Link link = new BookmarkablePageLink("link", Upload.class);
            link.add(file.createSmallThumb("thumb"));
            container.add(link);

            repeater.add(container);

            if (++itemCount % rowElements == 0) {
                container.add(AttributeModifier.append("class", "last"));
            }

            if (itemCount == maxElems) {
                break;
            }
        }

        add(repeater);
    }

}
