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
package com.apachecon.memories.hippocampus;

import com.apachecon.memories.speechbubble.ImageWriter;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ImageHandler.class);
    private static final String CONTENT_TYPE_PREFIX = "image/";
    private static final int DEF_THUMBNAIL_MAX = 200;

    public ImageHandler() {
        // Complete
    }

    public void writeAttachment(@Body DataHandler dh, @Header(value = "CamelFileName") String filename,
                                @Header(value = "CamelFileParent") String parent) throws IOException {

        // Substitute name if needed.
        String fn = dh.getName();
        if (!ObjectHelper.isEmpty(filename)) {
            int pos = fn.lastIndexOf('.');
            filename = pos < 0 ? filename : filename + fn.substring(pos);
        } else {
            filename = fn;
        }

        File out = !ObjectHelper.isEmpty(parent) ? new File(new File(parent), filename) : new File(filename);
        LOG.info("Writing '{}' attachment to file: {}", fn, out.getAbsolutePath());

        FileOutputStream fos = new FileOutputStream(out);
        dh.writeTo(fos);
        fos.flush();
        fos.close();
    }

    public void writeImage(@Body RenderedImage img, 
        @Header(value = "CamelFileName") String filename,
        @Header(value = "CamelFileParent") String parent,
        @Header(value = "Content-Type") String contentType) throws IOException {

        String ct = getContentType(contentType);
        if (ObjectHelper.isEmpty(ct) || !ct.startsWith(CONTENT_TYPE_PREFIX)) {
            LOG.warn("Content-Type not provided or invalid {}. Request ignored", contentType);
        }
        ImageWriter.write(img, filename, parent, ct.substring(CONTENT_TYPE_PREFIX.length()));
    }

    public void generateThumbnail(@Body GenericFile<File> img, 
        @Header(value = "MemoriesUploads") String target,
        @Header(value = "ThumbnailMaxSize") String maxSize) throws IOException {

        File source = img.getFile();
        File parent = new File(source.getParentFile().getParentFile(), target);
        LOG.debug("Generating thumbnail for {}", img.getAbsoluteFilePath());
        ImageWriter.generateThumbnail(source, parent, maxSize == null ? DEF_THUMBNAIL_MAX : Integer.parseInt(maxSize));
    }

    public static String getContentType(String contentType) {
        int semi = contentType == null ? -1 : contentType.indexOf(';');
        return semi >= 0 ? contentType.substring(0, semi) : contentType;
    }

    public static boolean isImage(String contentTypeHeader) {
        String contentType = getContentType(contentTypeHeader);
        int slash = contentType == null ? -1 : contentType.indexOf('/');
        return slash >= 0 && ("image".equals(contentType.substring(0, slash)) 
            || "application/octet-stream".equals(contentType));
    }
}
