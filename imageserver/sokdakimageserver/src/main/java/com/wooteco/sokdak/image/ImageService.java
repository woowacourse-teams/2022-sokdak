package com.wooteco.sokdak.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${image-dir}")
    private String imageDir;

    public ImageResponse uploadImage(MultipartFile multipartFile) {
        final Extension extension = Extension.from(extractExtension(multipartFile));
        final Image image = Image.of(imageDir, extension);
        image.save(multipartFile);
        if (extension.canNotCompress()) {
            return new ImageResponse(image.getName());
        }

        final String compressedImageName = image.compress();
        return new ImageResponse(compressedImageName);
    }

    private String extractExtension(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }
}
