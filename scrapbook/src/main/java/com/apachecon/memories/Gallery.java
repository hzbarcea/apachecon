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

import com.apachecon.memories.service.UserFile;
import com.apachecon.memories.util.Partition;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public class Gallery extends Panel {
	private static final long serialVersionUID = 1L;

	public Gallery(String id, IModel<List<UserFile>> model) {
        super(id);

        RepeatingView frames = new RepeatingView("frames");

        List<List<UserFile>> partitions = Partition.partition(model.getObject(), 12);
        int page = 0;

        for (List<UserFile> files : partitions) {
            WebMarkupContainer container = new WebMarkupContainer(frames.newChildId());
            frames.add(container);

            RepeatingView items = new RepeatingView("items");
            container.add(items);

            for (UserFile file : files) {
                WebMarkupContainer secondContainer = new WebMarkupContainer(items.newChildId());
                items.add(secondContainer);

                secondContainer.add(file.createSmallThumb("thumb"));

                enrich(secondContainer, file, page);
            }
            page++;
        }

        add(frames);
    }

    protected void enrich(WebMarkupContainer secondContainer, UserFile file, int page) {
    }
}
