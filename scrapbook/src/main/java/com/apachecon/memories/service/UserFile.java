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
package com.apachecon.memories.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;

public class UserFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private File file;
    private File thumb;
    private Boolean approved;

    public UserFile(final File file, Boolean approved) {
        this.thumb = file;
        this.file = new File(thumb.getParentFile().getParent(), "archive");
        final String stem = thumb.getName().substring(0, thumb.getName().length() - 4).toLowerCase();
        File files[] = this.file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.startsWith(stem) 
                    && (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif"));
            }
        });
        if (files != null && files.length > 0) {
            this.file = files[0];
        } else {
            this.file = thumb;
        }
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved == null ? false : approved;
    }

    public boolean isNew() {
        return approved == null;
    }

    private Image createImage(String id, boolean small) {
        File f = small ? thumb : file;
        
        IResource resource = new ResourceStreamResource(new FileResourceStream(f));
        return new Image(id, resource);
    }

    public Component createSmallThumb(String id) {
        return createImage(id, true);
    }

    public Component createBigThumb(String id) {
        return createImage(id, false);
    }

    public String getName() {
        return thumb.getName();
    }

    public String getType() {
        return getName().substring(getName().lastIndexOf('.') + 1);
    }
    
    public String getContentType() {
        // use file extension as content type
        return "image/" + getType();
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public static boolean generateThumbnail(File file, File thumb, int maxSize) throws IOException {
        if (thumb.exists()) {
            return false;
        }

    	BufferedImage i = ImageIO.read(new FileInputStream(file));
        int w = i.getWidth();
        int h = i.getHeight();
        int t = i.getType();

        int neww = w;
        int newh = h;
        boolean resize = false;
        if ((w > maxSize) || (h > maxSize)) {
        	resize = true;
            if (w > h) {
                neww = maxSize;
                newh = (maxSize * h) / w;
            } else {
                neww = (maxSize * w) / h;
                newh = maxSize;
            }
        }

        if (file.getParent() == thumb.getParent() && !resize && BufferedImage.TYPE_INT_RGB == t) {
        	return false;
        }

        BufferedImage bdest = new BufferedImage(neww, newh, t);
        Graphics2D g = bdest.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Force white background if converting from ARGB types
        g.setBackground(Color.WHITE);
        g.drawImage(i, 0, 0, neww , newh, null);
        g.dispose();
        
        String format = thumb.getName().substring(thumb.getName().lastIndexOf('.') + 1);
        ImageIO.write(bdest, format, thumb);
        return true;
    }
}
