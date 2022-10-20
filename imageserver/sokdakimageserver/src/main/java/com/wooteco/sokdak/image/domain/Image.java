package com.wooteco.sokdak.image.domain;

import com.wooteco.sokdak.image.util.ImageCompressor;
import com.wooteco.sokdak.image.exception.FileIOException;
import java.io.File;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class Image {

    private final String path;
    private final String name;
    private final Extension extension;

    private Image(String path, String name, Extension extension) {
        this.path = path;
        this.name = name;
        this.extension = extension;
    }

    public static Image of(String path, String extensionValue) {
        final Extension extension = Extension.from(extensionValue);
        final String imageName = UUID.randomUUID() + "." + extension.getValue();
        return new Image(path, imageName, extension);
    }

    public void save(MultipartFile image) {
        try {
            final File file = new File(path + name);
            image.transferTo(file);
        } catch (Exception exception) {
            throw new FileIOException();
        }
    }

    public String compress() {
        final String compressedImageName = "compressed-" + name;

        final File originalFile = new File(path + name);
        final File compressedFile = new File(path + compressedImageName);

        ImageCompressor.compress(originalFile, compressedFile, extension.getValue());

        return compressedImageName;
    }

    public boolean canNotCompress() {
        return extension == Extension.GIF;
    }

    public String getName() {
        return name;
    }
}
