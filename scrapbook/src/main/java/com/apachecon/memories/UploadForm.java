/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	private static final long serialVersionUID = 1L;

	private final List<String> contentTypes;
	private FileUploadField uploadField;

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