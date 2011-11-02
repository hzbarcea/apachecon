package com.apachecon.memories;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;

/**
 * Upload page - allows users to share their pictures.
 * 
 * @author lukasz
 */
public class Upload extends ScrapbookPage {

    private static final long serialVersionUID = 973033955774985294L;

    private MultiFileUploadField uploadField;

    private String[] contentTypes = new String[] {"image/jpeg", "image/jpg", "image/png", "image/gif"};


    public Upload(final PageParameters parameters) {
        add(new FeedbackPanel("feedback"));
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
                            InputStream is = upload.getInputStream();
                            File uploadDir = new File("target/uploads");
                            uploadDir.mkdirs();

                            File file = new File(uploadDir, upload.getClientFileName());
                            FileOutputStream os = new FileOutputStream(file);

                            byte[] buffer = new byte[512];
                            int length = 0;
                            while ((length = is.read(buffer)) > 0) {
                                os.write(buffer, 0, length);
                            }

                            is.close();
                            os.close();

                            Upload.this.info("File " + upload.getClientFileName() + " has been uploaded and now it waiting for approval");
                        } catch (IOException e) { 
                            Upload.this.error("An error occured during file upload.");
                        }
                    }
                }
            }
        };

        Model<ArrayList<FileUpload>> model = new Model<ArrayList<FileUpload>>(new ArrayList<FileUpload>());
        uploadField = new MultiFileUploadField("uploadField", model);
        form.add(uploadField);
        form.add(new SubmitLink("submit"));
        add(form);
    }
}
