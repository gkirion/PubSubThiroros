package com.george.pubsub.thiroros.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChordUtils {

    public static int computeId(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(str.getBytes("UTF-8"));
        int id = bytesToInt(digest);
        return id;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static int bytesToInt(byte[] hash) {
        String str = bytesToHex(hash);
        str = str.substring(56);
        int result = 0;
        for (int i = 0; i < 7; i++) {
            result = result + (Integer.parseInt(String.valueOf(str.charAt(i)), 16) << (i * 4));
        }
        result = result + ((Integer.parseInt(String.valueOf(str.charAt(7)), 16) & 0x07) << (7 * 4));
        return result;
    }

}
