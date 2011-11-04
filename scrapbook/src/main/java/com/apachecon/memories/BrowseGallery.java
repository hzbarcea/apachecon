package com.apachecon.memories;

import com.apachecon.memories.link.ImageLink;
import com.apachecon.memories.service.UserFile;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BrowseGallery extends Gallery {

    public BrowseGallery(String id, IModel<List<UserFile>> model) {
        super(id, model);
    }

    @Override
    protected void enrich(WebMarkupContainer secondContainer, UserFile file, int page) { 
        ImageLink link = new ImageLink("imageLink", file);
        link.add(AttributeModifier.append("rel", "page-" + page));
        secondContainer.add(link);
    }

}
