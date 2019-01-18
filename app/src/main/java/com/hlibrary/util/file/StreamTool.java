package com.hlibrary.util.file;

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool {

    public static byte[] readStream(@NonNull String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        return readStream(in);
    }

    public static byte[] readStream(@NonNull InputStream in) throws IOException {
        byte[] data = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(data)) != -1) {
            bos.write(data, 0, len);
        }
        return bos.toByteArray();
    }

}
