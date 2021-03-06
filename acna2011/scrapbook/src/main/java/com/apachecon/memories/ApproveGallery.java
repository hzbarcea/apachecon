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

import com.apachecon.memories.link.ApproveLink;
import com.apachecon.memories.link.DeclineLink;
import com.apachecon.memories.link.ImageLink;
import com.apachecon.memories.service.UserFile;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;

public class ApproveGallery extends Gallery {
	private static final long serialVersionUID = 1L;

	public ApproveGallery(String id, IModel<List<UserFile>> model) {
        super(id, model);
    }

    @Override
    protected void enrich(WebMarkupContainer secondContainer, UserFile file, int page) {
        secondContainer.add(new ImageLink("imageLink", file));

        final EmptyPanel decorator = new EmptyPanel("decorator");
        decorator.setOutputMarkupId(true);

        if (!file.isNew()) {
            // decorate files only if they come from approved/declined directory
            decorator.add(AttributeModifier.append("class", file.isApproved() ? "approved" : "declined"));
        }
        secondContainer.add(decorator);

        secondContainer.add(new ApproveLink("approve", file) {
			private static final long serialVersionUID = 1L;

			protected void update(AjaxRequestTarget target) {
                decorator.add(AttributeModifier.replace("class", "approved"));
                target.add(decorator);
            }
        });
        secondContainer.add(new DeclineLink("decline", file) {
			private static final long serialVersionUID = 1L;

			protected void update(AjaxRequestTarget target) {
                decorator.add(AttributeModifier.replace("class", "declined"));
                target.add(decorator);
            }
        });
    }

}
