package com.apachecon.memories.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public class DefaultImageService implements ImageService {

    private File aproveDirectory;
    private File declineDirectory;
    private File uploadDirectory;

    @Override
    public void newFile(FileUpload upload) throws Exception {
        String ext = upload.getClientFileName().substring(upload.getClientFileName().lastIndexOf('.'));
        File file = new File(uploadDirectory, UUID.randomUUID() + ext);

        InputStream is = upload.getInputStream();
        FileOutputStream os = new FileOutputStream(file);

        byte[] buffer = new byte[512];
        int length = 0;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        is.close();
        os.close();
    }

    @Override
    public List<File> getAproved() {
        return list(aproveDirectory.listFiles());
    }

    @Override
    public List<File> getDecline() {
        return list(declineDirectory.listFiles());
    }

    private List<File> list(File[] listFiles) {
        if (listFiles == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(listFiles);
    }

    @Override
    public List<File> getAll() {
        List<File> files = new ArrayList<File>();
        files.addAll(getAproved());
        files.addAll(getDecline());
        Collections.shuffle(files);
        return files;
    }

    public void setAproveDirectory(File directory) {
        this.aproveDirectory = directory;
    }

    public void setDeclineDirectory(File directory) {
        this.declineDirectory = directory;
    }

    public void setUploadDirectory(File directory) {
        this.uploadDirectory = directory;
    }

}
