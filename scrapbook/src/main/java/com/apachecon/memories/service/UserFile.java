package com.apachecon.memories.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;

public class UserFile implements Serializable {

    private File file;
    private Boolean approved;

    public UserFile(File file, Boolean approved) {
        this.file = file;
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved == null ? false : approved;
    }

    public boolean isNew() {
        return approved == null;
    }

    private Image createImage(String id, boolean small) {
        IResource resource = new ResourceStreamResource(new FileResourceStream(file));
        if (small) {
            resource = new ThumbnailImageResource(resource, 180);
        }
        return new Image(id, resource);
    }

    public Component createSmallThumb(String id) {
        return createImage(id, true);
    }

    public Component createBigThumb(String id) {
        return createImage(id, false);
    }

    public String getName() {
        return file.getName();
    }

    public String getContentType() {
        // use file extension as content type
        return "image/"+ getName().substring(getName().lastIndexOf('.') + 1);
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

}
