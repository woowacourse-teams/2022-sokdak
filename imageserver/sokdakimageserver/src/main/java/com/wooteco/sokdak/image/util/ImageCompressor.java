package com.wooteco.sokdak.image.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.wooteco.sokdak.image.exception.ImageReadException;
import java.awt.Graphics2D;
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
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

public class ImageCompressor {

    public static void compress(File originalFile, File compressedFile, String extension) {
        try (final OutputStream outputStream = new FileOutputStream(compressedFile);
             final ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {

            final BufferedImage bufferedImage = extractBufferedImage(originalFile);

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

    private static BufferedImage extractBufferedImage(File originalFile) {
        final BufferedImage bufferedImage = rotateIfUnexpectedRotation(originalFile);
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return bufferedImage;
        }

        final BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = newBufferedImage.createGraphics();
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        return newBufferedImage;
    }

    private static BufferedImage rotateIfUnexpectedRotation(File originalFile) {
        try {
            final BufferedImage bufferedImage = ImageIO.read(originalFile);
            final Metadata metadata = ImageMetadataReader.readMetadata(originalFile);
            final ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory == null || !directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                return bufferedImage;
            }

            return Scalr.rotate(bufferedImage, Rotation.CW_90, null);
        } catch (Exception exception) {
            throw new ImageReadException();
        }
    }
}
