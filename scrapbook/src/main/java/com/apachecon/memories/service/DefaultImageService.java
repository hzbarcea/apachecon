package com.apachecon.memories.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public class DefaultImageService implements ImageService {

    private static final FilenameFilter FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif");
        }
    };

    private File approveDirectory;
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
        os.close();
        is.close();
    }

    @Override
    public List<UserFile> getUploaded() {
        return list(uploadDirectory.listFiles(FILTER), null);
    }

    @Override
    public List<UserFile> getApproved() {
        return list(approveDirectory.listFiles(FILTER), true);
    }

    @Override
    public List<UserFile> getDeclined() {
        return list(declineDirectory.listFiles(FILTER), false);
    }

    private List<UserFile> list(File[] listFiles, Boolean approved) {
        if (listFiles == null) {
            return Collections.emptyList();
        }

        List<UserFile> files = new ArrayList<UserFile>();
        for (File file : listFiles) {
            files.add(new UserFile(file, approved));
        }
        return files;
    }

    @Override
    public List<UserFile> getAll() {
        List<UserFile> files = new ArrayList<UserFile>();
        files.addAll(getUploaded());
        files.addAll(getApproved());
        files.addAll(getDeclined());
        //Collections.shuffle(files);
        return files;
    }

    public void setApproveDirectory(File directory) {
        this.approveDirectory = directory;
    }

    public void setDeclineDirectory(File directory) {
        this.declineDirectory = directory;
    }

    public void setUploadDirectory(File directory) {
        this.uploadDirectory = directory;
    }

}
