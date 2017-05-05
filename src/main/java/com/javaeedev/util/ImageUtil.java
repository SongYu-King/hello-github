package com.javaeedev.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Create preview image.
 * 
 * @author Xuefeng
 */
public class ImageUtil {

    private static final int IMAGE_WIDTH = 48;
    private static final int IMAGE_HEIGHT = 48;
    private static final int MIN = 5;

    private static Log log = LogFactory.getLog(ImageUtil.class);

    public static void createPreviewImage(InputStream srcInput, File destFile, int maxWidthOrHeight) {
        if(srcInput==null)
            throw new NullPointerException("Null input stream.");
        BufferedImage bis = null;
        try {
            bis = ImageIO.read(srcInput);
            if(bis==null)
                throw new IllegalArgumentException("Cannot load image.");
        }
        catch(IOException ioe) {
            throw new IllegalArgumentException("Cannot load image.", ioe);
        }
        int targetWidth = bis.getWidth();
        int targetHeight = bis.getHeight();
        if(targetWidth > maxWidthOrHeight) {
            targetHeight = maxWidthOrHeight * targetHeight / targetWidth;
            targetWidth = maxWidthOrHeight;
        }
        if(targetHeight > maxWidthOrHeight) {
            targetWidth =  maxWidthOrHeight * targetWidth / targetHeight;
            targetHeight = maxWidthOrHeight;
        }
        if(targetWidth < MIN)
            targetWidth = MIN;
        if(targetHeight < MIN)
            targetHeight = MIN;
        try {
            BufferedImage bid = null;
            bid = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bid.createGraphics();
            g.drawImage(bis, 0, 0, targetWidth, targetHeight, null);
            g.dispose();
            ImageIO.write(bid, "jpg", destFile);
        }
        catch(IOException e) {
            log.warn("Create preview image failed.", e);
            throw new RuntimeException(e);
        }
    }

    public static void createPreviewImage(InputStream srcInput, File destFile, int targetWidth, int targetHeight) {
        if(srcInput==null)
            throw new NullPointerException("Null input stream.");
        BufferedImage bis = null;
        try {
            bis = ImageIO.read(srcInput);
            if(bis==null)
                throw new IllegalArgumentException("Cannot load image.");
        }
        catch(IOException ioe) {
            throw new IllegalArgumentException("Cannot load image.", ioe);
        }
        try {
            BufferedImage bid = null;
            bid = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bid.createGraphics();
            g.drawImage(bis, 0, 0, targetWidth, targetHeight, null);
            g.dispose();
            ImageIO.write(bid, "jpg", destFile);
        }
        catch(IOException e) {
            log.warn("Create preview image failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a preview image from an Image-InputStream, and close the InputStream.
     */
    public static void createPreviewImage(InputStream srcInput, File destFile) {
        createPreviewImage(srcInput, destFile, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

}
