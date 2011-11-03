package com.apachecon.memories;

import com.apachecon.memories.approve.ApproveRequest;
import com.apachecon.memories.approve.DeclineRequest;
import com.apachecon.memories.util.Partition;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;

public class Gallery extends Panel {

    private transient Logger logger = LoggerFactory.getLogger(Gallery.class);

    public Gallery(String id, List<File> model) {
        super(id);

        RepeatingView frames = new RepeatingView("frames");

        List<List<File>> partitions = Partition.partition(model, 12);

        for (List<File> files : partitions) {
            WebMarkupContainer container = new WebMarkupContainer(frames.newChildId());
            frames.add(container);

            RepeatingView items = new RepeatingView("items");
            container.add(items);

            for (File file : files) {
                final WebMarkupContainer secondContainer = new WebMarkupContainer(items.newChildId());
                secondContainer.setOutputMarkupId(true);
                items.add(secondContainer);

                ResourceStreamResource imageResource = new ResourceStreamResource(new FileResourceStream(file));
                secondContainer.add(new Image("thumb", imageResource));
                secondContainer.add(new ImageLink("imageLink", file));

                secondContainer.add(new AjaxLink<File>("approve", Model.of(file)) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        File modelObject = getModelObject();

                        try {
                            ApproveRequest rq = new ApproveRequest();
                            rq.setFileName(modelObject.getName());
                            ((ScrapbookApplication) getApplication()).getApprovalService().approve(rq);
                        } catch (Exception e) {
                            logger.error("Error processing web service approve request", e);
                        }

                        target.add(secondContainer);
                    }
                });
                secondContainer.add(new AjaxLink<File>("decline", Model.of(file)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        File modelObject = getModelObject();

                        try {
                            DeclineRequest rq = new DeclineRequest();
                            rq.setFileName(modelObject.getName());
                            ((ScrapbookApplication) getApplication()).getApprovalService().decline(rq);
                        } catch (Exception e) {
                            logger.error("Error processing web service decline request", e);
                        }

                        target.add(secondContainer);
                    }
                });
            }
        }

        add(frames);
    }

}
