package com.apachecon.memories.session;

import java.security.MessageDigest;

/**
 * Small utility class which convert md5 digest into typical string, without binary entries.
 * 
 * @author lukasz
 */
public class MD5Util {

    public static String encode(String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(in.getBytes());

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                    String text = Integer.toHexString(0xFF & digest[i]);
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
