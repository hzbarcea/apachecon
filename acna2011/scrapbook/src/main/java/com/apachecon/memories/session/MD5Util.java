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
package com.apachecon.memories.session;

import java.security.MessageDigest;

/**
 * Small utility class which convert md5 digest into typical string, without
 * binary entries.
 * 
 * @author lukasz
 */
public class MD5Util {

    public static String encode(String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(in.getBytes());

            StringBuffer hexString = new StringBuffer();
            for (byte element : digest) {
                String text = Integer.toHexString(0xFF & element);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                hexString.append(text);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
