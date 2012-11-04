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

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class Thumbs extends Panel {

    public Thumbs(String id, int maxElems, int rowElements, IModel<List<UserFile>> model) {
        super(id, model);

        RepeatingView repeater = new RepeatingView("items");
        int itemCount = 0;
        for (UserFile file : model.getObject()) {
            MarkupContainer container = new WebMarkupContainer(repeater.newChildId());
            Link link = new BookmarkablePageLink("link", Upload.class);
            link.add(file.createSmallThumb("thumb"));
            container.add(link);

            repeater.add(container);

            if (++itemCount % rowElements == 0) {
                container.add(AttributeModifier.append("class", "last"));
            }

            if (itemCount == maxElems) {
                break;
            }
        }

        add(repeater);
    }

}
