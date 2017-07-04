package com.ddq.lib.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dongdaqing on 2017/4/24.
 */

public class EncryptUtil {
    private static String getChecksum(MessageDigest digest, InputStream is) throws IOException {
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = is.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        is.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }

        //return complete hash
        return sb.toString();
    }

    public static String getStringChecksum(String s, String method) {
        ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes());
        try {
            MessageDigest digest = MessageDigest.getInstance(method);
            return getChecksum(digest, new ByteArrayInputStream(s.getBytes()));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MD5(String s){
        return getStringChecksum(s,"MD5");
    }
}
