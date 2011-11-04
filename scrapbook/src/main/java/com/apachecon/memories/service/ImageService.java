package com.apachecon.memories.service;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public interface ImageService {

    void newFile(FileUpload upload) throws Exception;

    List<UserFile> getUploaded();

    List<UserFile> getApproved();

    List<UserFile> getDeclined();

    List<UserFile> getAll();

}
