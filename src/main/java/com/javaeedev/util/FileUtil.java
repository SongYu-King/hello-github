package com.javaeedev.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Read and write file.
 * 
 * @author Xuefeng
 */
public class FileUtil {

    private static final int MAX_BUFFER_SIZE = 4096;

    public static boolean removeFile(File file) {
        if(!file.isFile())
            return false;
        for(int i=0; i<3; i++) {
            if(file.delete())
                return true;
        }
        return false;
    }

    public static byte[] readFile(File file) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        BufferedInputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[2048];
            for(;;) {
                int len = input.read(buffer);
                if(len==(-1))
                    break;
                output.write(buffer, 0, len);
            }
        }
        finally {
            if(input!=null)
                close(input);
        }
        return output.toByteArray();
    }

    public static void readFile(File file, OutputStream output) throws IOException {
        FileInputStream input = null;
        FileChannel fc = null;
        try {
            input = new FileInputStream(file);
            fc = input.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for(;;) {
                buffer.clear();
                int n = fc.read(buffer);
                if(n==(-1))
                    break;
                output.write(buffer.array(), 0, buffer.position());
            }
            output.flush();
        }
        finally {
            if(fc!=null)
                close(fc);
            if(input!=null)
                close(input);
        }
    }

    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream input = null;
        FileChannel inputChannel = null;
        FileOutputStream output = null;
        FileChannel outputChannel = null;
        try {
            input = new FileInputStream(src);
            inputChannel = input.getChannel();
            output = new FileOutputStream(dest);
            outputChannel = output.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            for(;;) {
                buffer.clear();
                if((-1)==inputChannel.read(buffer))
                    break;
                buffer.flip();
                outputChannel.write(buffer);
            }
        }
        finally {
            if(outputChannel!=null)
                close(outputChannel);
            if(output!=null)
                close(output);
        }
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        FileOutputStream output = null;
        FileChannel fc = null;
        try {
            output = new FileOutputStream(file);
            fc = output.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            int offset = 0;
            while(offset<data.length) {
                buffer.clear();
                int len = data.length - offset;
                if(len>MAX_BUFFER_SIZE)
                    len = MAX_BUFFER_SIZE;
                buffer.put(data, offset, len);
                offset += len;
                buffer.flip();
                fc.write(buffer);
            }
        }
        finally {
            if(fc!=null)
                close(fc);
            if(output!=null)
                close(output);
        }
    }

    private static void close(InputStream input) {
        if(input!=null) {
            try {
                input.close();
            }
            catch(IOException e) {}
        }
    }

    private static void close(OutputStream output) {
        if(output!=null) {
            try {
                output.close();
            }
            catch(IOException e) {}
        }
    }

    private static void close(FileChannel channel) {
        if(channel!=null) {
            try {
                channel.close();
            }
            catch(IOException e) {}
        }
    }
}
