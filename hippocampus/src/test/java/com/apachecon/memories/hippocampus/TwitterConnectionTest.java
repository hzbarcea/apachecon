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

import java.util.Calendar;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.apachecon.memories.twitter.Metadata;
import com.apachecon.memories.twitter.Search;
import com.apachecon.memories.twitter.Tweet;


public class TwitterConnectionTest extends CamelSpringTestSupport {

    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Test
    public void testTweets() throws Exception {
    	Metadata m = new Metadata();
    	m.setResult_type("recent");

    	Tweet t = new Tweet();
    	t.setCreated_at(Calendar.getInstance().getTime());
    	t.setFrom_user("Penguinista");
    	t.setFrom_user_id(5937811L);
    	t.setFrom_user_id_str("5937811");
        t.setGeo(null);
        t.setId(131094616308056064L);
        t.setId_str("131094616308056064");
        t.setIso_language_code("en");
        t.setMetadata(m);
        t.setProfile_image_url("http://a1.twimg.com/profile_images/1158329686/AB_Twit_normal.jpg");
        t.setSource("&lt;a href=&quot;http://www.tweetdeck.com&quot; rel=&quot;nofollow&quot;&gt;TweetDeck&lt;/a&gt;");
        t.setText("RT @ivanristic: RT @jimjag: benchmarking #nginx 1.1.6 and #apache #httpd 2.3.15-dev\u2026 Apache has quicker transaction times! Will describe at #apachecon");
        t.setTo_user(null);
        t.setTo_user_id(null);
        t.setTo_user_id_str("null");

    	Search s = new Search();
    	s.setCompleted_in(0.175);
    	s.setMax_id(131094616308056064L);
    	s.setMax_id_str("131094616308056064");
    	s.setPage(1);
    	s.setQuery("%23apachecon");
    	s.setRefresh_url("?since_id=131094616308056064&q=%23apachecon");
    	s.getResults().add(t);
    	s.setResults_per_page(15);
    	s.setSince_id(131073333025447936L);
    	s.setSince_id_str("131073333025447936");
    	
        template.sendBody("seda:tweets", "Hello world");
/*
    	MockEndpoint mock = (MockEndpoint)context.getEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);
        mock.assertIsSatisfied();
*/
        // assertTrue(mock.assertExchangeReceived(0).getIn().getBody() instanceof Tweet);
        // s = mock.assertExchangeReceived(0).getIn().getBody(Search.class);
        Thread.sleep(5000);
    }
}
