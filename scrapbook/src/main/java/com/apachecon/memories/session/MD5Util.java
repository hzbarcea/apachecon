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
