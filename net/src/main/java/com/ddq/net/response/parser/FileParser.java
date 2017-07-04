package com.ddq.net.response.parser;

import com.ddq.architect.view.ICount;
import com.ddq.net.exception.ParseException;
import com.ddq.net.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dongdaqing on 2017/7/3.
 * 下载文件
 */

public class FileParser implements Parser<File> {
    private String path;//文件的完整路径
    private ICount mProgress;

    public FileParser(String path) {
        this(path, null);
    }

    public FileParser(String path, ICount progress) {
        this.path = path;
        mProgress = progress;
    }

    @Override
    public File parse(InputStream is, int length) throws ParseException {
        File file = new File(path);
        if (!file.isFile()) {
            file = new File(path, String.valueOf(System.currentTimeMillis()));
        }

        File tmp = new File(file.getPath() + ".tmp");

        if (tmp.exists() && !tmp.delete()) {
            throw new ParseException("failed to delete tmp file:" + tmp.getPath());
        }
        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                throw new ParseException("failed to create file:" + tmp.getPath());
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int last = 0, c, ct = 0;
            while ((c = is.read(buffer)) != -1) {
                ct += c;
                fos.write(buffer, 0, c);
                dispatchProgress(ct, length);
            }
            if (!tmp.renameTo(file)) {
                throw new ParseException("failed to rename file:" + tmp.getPath());
            }
            return file;
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        } finally {
            IOUtil.close(fos);
        }
    }

    private void dispatchProgress(int current, int total) {
        if (mProgress != null) {
            mProgress.update(current, total);
        }
    }
}
