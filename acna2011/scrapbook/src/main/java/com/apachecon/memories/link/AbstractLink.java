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
package com.apachecon.memories.link;

import com.apachecon.memories.ScrapbookApplication;
import com.apachecon.memories.service.ImageService;
import com.apachecon.memories.service.UserFile;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLink extends AjaxLink<UserFile> {
	private static final long serialVersionUID = 1L;

	protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractLink(String id, UserFile model) {
        super(id, Model.of(model));
    }

    public final void onClick(AjaxRequestTarget target) {
        try {
            call(ScrapbookApplication.getImageService());
        } catch (Exception e) {
            logger.error("Error calling web service", e);
        }
        update(target);
    }

    // for anonymous classes
    protected void update(AjaxRequestTarget target) {
        
    }

    public abstract void call(ImageService service);

}
