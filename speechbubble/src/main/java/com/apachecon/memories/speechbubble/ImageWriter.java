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
package com.apachecon.memories.speechbubble;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageWriter {
    private static final Logger LOG = LoggerFactory.getLogger(ImageWriter.class);

    private ImageWriter() {
        // Non-instantiable utility
    }

    public static void write(RenderedImage img, String filename, String parent, String imageFormat)
        throws IOException {
        if (img == null) {
            LOG.warn("Image not provided. Write aborted");
            return;
        }
        if (parent == null || filename == null || imageFormat == null) {
            LOG.warn("Target filename or Content-Type not provided. Write aborted");
            return;
        }
        File target = new File(parent);
        if (!target.isDirectory()) {
            target = target.getParentFile();
        }
        if (!target.exists()) {
            LOG.warn("Target directory does not exist ({}). Write aborted", target.getAbsolutePath());
            return;
        }
        File output = new File(target, filename + "." + imageFormat);
        LOG.debug("Writing '{}' image to file: {}", imageFormat, output.getAbsolutePath());
        ImageIO.write(img, imageFormat, output);
    }

    public static void generateThumbnail(File source, File parent, int maxSize) throws IOException {
    	String fn = source.getName().substring(0, source.getName().lastIndexOf('.'));
    	File thumb = new File(parent, fn + ".jpg");
        if (thumb.exists()) {
            LOG.debug("Thumbnail already present for {}", thumb.getAbsoluteFile());
            return;
        }

        BufferedImage i = ImageIO.read(new FileInputStream(source));
        int w = i.getWidth();
        int h = i.getHeight();

        int neww = w;
        int newh = h;
        if ((w > maxSize) || (h > maxSize)) {
            if (w > h) {
                neww = maxSize;
                newh = (maxSize * h) / w;
            } else {
                neww = (maxSize * w) / h;
                newh = maxSize;
            }
        }

        BufferedImage bdest = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bdest.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Force white background if converting from ARGB types
        g.setBackground(Color.WHITE);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, neww, newh);
        g.drawImage(i, 0, 0, neww , newh, null);
        g.dispose();
        ImageIO.write(bdest, "jpg", thumb);
        LOG.debug("Thumbnail generated at {}", thumb.getAbsoluteFile());
    }
}
