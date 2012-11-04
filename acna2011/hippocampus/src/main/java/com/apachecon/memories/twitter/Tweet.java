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
package com.apachecon.memories.twitter;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date created_at;
    private String from_user;
    private Long from_user_id;
    private String from_user_id_str;
    private Geo geo;
    private Long id;
    private String id_str;
    private String iso_language_code;
    private Metadata metadata;
    private String profile_image_url;
    private String source;
    private String text;
    private String to_user;
    private Long to_user_id;
    private String to_user_id_str;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public Long getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(Long from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getFrom_user_id_str() {
        return from_user_id_str;
    }

    public void setFrom_user_id_str(String from_user_id_str) {
        this.from_user_id_str = from_user_id_str;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getIso_language_code() {
        return iso_language_code;
    }

    public void setIso_language_code(String iso_language_code) {
        this.iso_language_code = iso_language_code;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public Long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(Long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getTo_user_id_str() {
        return to_user_id_str;
    }

    public void setTo_user_id_str(String to_user_id_str) {
        this.to_user_id_str = to_user_id_str;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
