/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apachecon.memories.cxf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import com.apachecon.memories.cxf.ImageService.ImageList;

/**
 * 
 */

@Path("/")
@WebService(endpointInterface = "com.apachecon.memories.cxf.ImageService")
@BindingType(SOAPBinding.SOAP12HTTP_MTOM_BINDING)
public class UploadService {
    private static final FilenameFilter FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif");
        }
    };

    
    File uploadPath;
    File approvePath;
    public UploadService(File p) {
        uploadPath = new File(p, "upload");
        approvePath = new File(p, "approve");
        uploadPath.mkdirs();
        approvePath.mkdirs();
    }
    
    public String upload(DataHandler image) throws IOException {
        String s = image.getContentType().toLowerCase();
        if (s == null) {
            s = "image/jpg";
        }
        if (s.endsWith("jpg")) {
            return uploadJPG(IOUtils.readBytesFromStream(image.getDataSource().getInputStream()));
        } else if (s.endsWith("gif")) {
            return uploadGIF(IOUtils.readBytesFromStream(image.getDataSource().getInputStream()));
        } else if (s.endsWith("png")) {
            return uploadPNG(IOUtils.readBytesFromStream(image.getDataSource().getInputStream()));
        }
        return null;
    }
    
    
    @POST
    @Consumes("image/jpg")
    @Produces("text/plain")
    @Path("/upload")
    public String uploadJPG(byte[] image) throws IOException {
        File f = File.createTempFile("cxfupload", ".jpg", uploadPath);
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(image);
        fout.close();
        return f.getName();
    }
    @POST
    @Consumes("image/png")
    @Produces("text/plain")
    @Path("/upload")
    public String uploadPNG(byte[] image) throws IOException {
        File f = File.createTempFile("cxfupload", ".png", uploadPath);
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(image);
        fout.close();
        return f.getName();
    }
    @POST
    @Consumes("image/gif")
    @Produces("text/plain")
    @Path("/upload")
    public String uploadGIF(byte[] image) throws IOException {
        File f = File.createTempFile("cxfupload", ".gif", uploadPath);
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(image);
        fout.close();
        return f.getName();
    }
    
    @GET
    @Path("/uploads")
    @Produces({"text/xml", "application/json"})
    public ImageList listUploadedImages() {
        return new ImageList(uploadPath.list(FILTER));
    }

    @GET
    @Path("/approved")
    @Produces({"text/xml", "application/json"})
    public ImageList listApprovedImages() {
        return new ImageList(approvePath.list(FILTER));
    }

    @GET
    @Path("/approved/{img}")
    @Produces({"image/jpg", "image/png", "image/gif"})
    public Response getApprovedImageRest(@PathParam("img") String name) throws IOException {
        File f = new File(approvePath, name);
        byte bytes[] = IOUtils.readBytesFromStream(new FileInputStream(f));
        int idx = name.lastIndexOf('.');
        return Response.ok(bytes, "image/" + name.substring(idx + 1)).build();
    }
    public DataHandler getApprovedImage(final String name) throws IOException {
        File f = new File(approvePath, name);
        final int idx = name.lastIndexOf('.');
        return new DataHandler(new FileDataSource(f) {
            public String getContentType() {
                return "image/" + name.substring(idx + 1);
            }
        });
    }
    
    
    @GET
    @Path("/uploads/{img}")
    @Produces({"image/jpg", "image/png", "image/gif"})
    public Response getUploadedImageRest(@PathParam("img") String name) throws IOException {
        File f = new File(uploadPath, name);
        byte bytes[] = IOUtils.readBytesFromStream(new FileInputStream(f));
        int idx = name.lastIndexOf('.');
        return Response.ok(bytes, "image/" + name.substring(idx + 1)).build();
    }
    public DataHandler getUploadedImage(final String name) throws IOException {
        File f = new File(uploadPath, name);
        final int idx = name.lastIndexOf('.');
        return new DataHandler(new FileDataSource(f) {
            public String getContentType() {
                return "image/" + name.substring(idx + 1);
            }
        });
    }


    
    //for testing
    public static void main(String args[]) {
        JAXRSServerFactoryBean f = new JAXRSServerFactoryBean();
        f.setAddress("http://localhost:9000/memories");
        UploadService service = new UploadService(new File("/tmp/memories"));
        f.setResourceProvider(new SingletonResourceProvider(service));
        f.create();
        
        Endpoint.publish("http://localhost:9000/soap-memories", service);
    }
    
}
