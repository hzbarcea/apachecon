package com.apachecon.memories;

import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadForm extends Form<Void> {

    private FileUploadField uploadField;
    private final List<String> contentTypes;

    private transient Logger logger = LoggerFactory.getLogger(UploadForm.class);

    public UploadForm(String id, List<String> contentTypes) {
        super(id);
        this.contentTypes = contentTypes;

        setMultiPart(true);
        setMaxSize(Bytes.megabytes(2));

        add(uploadField = new FileUploadField("uploadField"));
        uploadField.setRequired(true);
        add(new SubmitLink("submit"));
    }

    @Override
    protected void onSubmit() {
//        for (FileUpload upload : uploadField.getFileUploads()) {
            FileUpload upload = uploadField.getFileUpload();
            if (!contentTypes.contains(upload.getContentType())) {
                warn("File " + upload.getClientFileName() + " is not supported. Only images can be shared");
            } else {
                try {
                    ScrapbookApplication.getImageService().newFile(upload);

                    info("File " + upload.getClientFileName() + " has been uploaded and now it waiting for approval");
                } catch (Exception e) {
                    logger.error("Error processing uploaded file", e);
                    error("An error occured during file ");
                }
//            }
        }
    }
}