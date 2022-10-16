package com.wooteco.sokdak.image.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageCompressor {

    public static void compress(File originalFile, File compressedFile, String extension) {
        try (final OutputStream outputStream = new FileOutputStream(compressedFile);
             final ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
            final BufferedImage bufferedImage = ImageIO.read(originalFile);
            final Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
            final ImageWriter writer = writers.next();

            writer.setOutput(imageOutputStream);
            final ImageWriteParam defaultWriteParam = writer.getDefaultWriteParam();
            if (defaultWriteParam.canWriteCompressed()) {
                defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                defaultWriteParam.setCompressionQuality(0.4f);
            }

            writer.write(null, new IIOImage(bufferedImage, null, null), defaultWriteParam);
        } catch (Exception exception) {
            return;
        }
        originalFile.delete();
    }
}
