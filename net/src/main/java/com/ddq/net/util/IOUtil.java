package com.ddq.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public class IOUtil {
    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readString(InputStream is) {
        final int increment = 8192;
        byte[] bytes = new byte[0];
        byte[] tmp = new byte[increment];
        int c = 0;
        try {
            while ((c = is.read(tmp)) != -1) {
                byte[] tmp1 = new byte[c + bytes.length];
                System.arraycopy(bytes, 0, tmp1, 0, bytes.length);
                System.arraycopy(tmp, 0, tmp1, bytes.length, c);
                bytes = tmp1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }
}
