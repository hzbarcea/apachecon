package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;
import com.apachecon.memories.service.ImageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

/**
 * Upload page - allows users to share their pictures.
 * 
 * @author lukasz
 */
public class Upload extends ScrapbookPage {

    private MultiFileUploadField uploadField;

    private String[] contentTypes = new String[] {"image/jpeg", "image/jpg", "image/png", "image/gif"};

    public Upload() {
        add(new FeedbackPanel("feedback"));

        ApprovedModel model = new ApprovedModel();
        add(new Thumbs("thumbs", 12, 4, model));
        add(new BookmarkablePageLink("browse", Browse.class));

        Form<Void> form = new Form<Void>("uploadForm") {
            {
                setMultiPart(true);
                setMaxSize(Bytes.megabytes(2));
            }

            @Override
            protected void onSubmit() {
                Collection<FileUpload> uploaded = uploadField.getModel().getObject();

                for (FileUpload upload : uploaded) {
                    if (Arrays.binarySearch(contentTypes, upload.getContentType()) == -1) {
                        Upload.this.warn("File " + upload.getClientFileName() + " is not supported. Only images can be shared");
                    } else {
                        try {
                            ScrapbookApplication.getImageService().newFile(upload);

                            Upload.this.info("File " + upload.getClientFileName() + " has been uploaded and now it waiting for approval");
                        } catch (Exception e) {
                            logger.error("Error processing uploaded file", e);
                            Upload.this.error("An error occured during file upload.");
                        }
                    }
                }
            }
        };

        Model<ArrayList<FileUpload>> uploadModel = new Model<ArrayList<FileUpload>>(new ArrayList<FileUpload>());
        uploadField = new MultiFileUploadField("uploadField", uploadModel);
        form.add(uploadField);
        form.add(new SubmitLink("submit"));
        add(form);
    }
}
