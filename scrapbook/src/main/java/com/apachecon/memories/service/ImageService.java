package com.apachecon.memories.service;

import java.io.File;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public interface ImageService {

    void newFile(FileUpload upload) throws Exception;

    List<File> getAproved();

    List<File> getDecline();

    List<File> getAll();

}
