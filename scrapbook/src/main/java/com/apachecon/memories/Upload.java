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
package com.apachecon.memories;

import com.apachecon.memories.model.ApprovedModel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Upload page - allows users to share their pictures.
 * 
 * @author lukasz
 */
public class Upload extends ScrapbookPage {
	private static final long serialVersionUID = 1L;

	private static final List<String> contentTypes = Arrays.asList("image/jpg", "image/jpg", "image/png", "image/gif");

    public Upload() {
        add(new FeedbackPanel("feedback"));

        ApprovedModel model = new ApprovedModel();
        add(new Thumbs("thumbs", 12, 4, model));
        add(new BookmarkablePageLink<Browse>("browse", Browse.class));

        add(new UploadForm("uploadForm", contentTypes));
    }
}
