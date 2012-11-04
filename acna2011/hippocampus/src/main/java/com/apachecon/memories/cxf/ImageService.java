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

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 * 
 */
@WebService
@BindingType(SOAPBinding.SOAP12HTTP_MTOM_BINDING)
public interface ImageService {
    
    String upload(@XmlMimeType("image/*") DataHandler image);
    
    public ImageList listApprovedImages();
    public ImageList listUploadedImages();
    
    @XmlMimeType("image/*") 
    DataHandler getApprovedImage(@WebParam(name = "img") String s);
    
    @XmlMimeType("image/*") 
    DataHandler getUploadedImage(@WebParam(name = "img") String s);

    
    @XmlRootElement(name = "images")
    static class ImageList {
        @XmlElement(name = "image")
        String images[];
        
        public ImageList() {
        }
        public ImageList(String i[]) {
            images = i;
        }
    }
}
