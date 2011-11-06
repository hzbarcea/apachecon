package com.apachecon.memories;

import com.apachecon.memories.link.ApproveLink;
import com.apachecon.memories.link.DeclineLink;
import com.apachecon.memories.link.ImageLink;
import com.apachecon.memories.service.UserFile;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;

public class ApproveGallery extends Gallery {
	private static final long serialVersionUID = 1L;

	public ApproveGallery(String id, IModel<List<UserFile>> model) {
        super(id, model);
    }

    @Override
    protected void enrich(WebMarkupContainer secondContainer, UserFile file, int page) {
        secondContainer.add(new ImageLink("imageLink", file));

        final EmptyPanel decorator = new EmptyPanel("decorator");
        decorator.setOutputMarkupId(true);

        if (!file.isNew()) {
            // decorate files only if they come from approved/declined directory
            decorator.add(AttributeModifier.append("class", file.isApproved() ? "approved" : "declined"));
        }
        secondContainer.add(decorator);

        secondContainer.add(new ApproveLink("approve", file) {
			private static final long serialVersionUID = 1L;

			protected void update(AjaxRequestTarget target) {
                decorator.add(AttributeModifier.replace("class", "approved"));
                target.add(decorator);
            }
        });
        secondContainer.add(new DeclineLink("decline", file) {
			private static final long serialVersionUID = 1L;

			protected void update(AjaxRequestTarget target) {
                decorator.add(AttributeModifier.replace("class", "declined"));
                target.add(decorator);
            }
        });
    }

}
