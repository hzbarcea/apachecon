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
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;

public class UserFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private File file;
    private File thumb;
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
        File f = small ? thumb : file;
        if (f == null) {
            File f2 = new File(file.getParentFile(), file.getName() + "_thumb");
            if (!f2.exists()) {
                try {
                    BufferedImage i = ImageIO.read(new FileInputStream(file));
                    int w = i.getWidth();
                    int h = i.getHeight();
                    int maxSize = 200;
    
                    if ((w > maxSize) || (h > maxSize)) {
                        int neww;
                        int newh;
    
                        if (w > h) {
                            neww = maxSize;
                            newh = (maxSize * h) / w;
                        } else {
                            neww = (maxSize * w) / h;
                            newh = maxSize;
                        }
    
                        BufferedImage bdest =
                            new BufferedImage(neww, newh, i.getType());
                        
                        Graphics2D g = bdest.createGraphics();
    
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                         
                        g.drawImage(i, 0, 0, neww , newh, null);
                        g.dispose();
                        
                        ImageIO.write(bdest, getType(), f2);
                        thumb = f2;
                    } else {
                        thumb = file;
                    }
                } catch (IOException ex) {
                    thumb = file;
                }
            } else {
                thumb = f2;
            }
            f = thumb;
        }
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
        return file.getName();
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

}
