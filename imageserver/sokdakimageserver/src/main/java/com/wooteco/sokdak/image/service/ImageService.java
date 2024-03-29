package com.wooteco.sokdak.image.service;

import com.wooteco.sokdak.image.domain.Image;
import com.wooteco.sokdak.image.dto.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${image-dir}")
    private String imageDir;

    public ImageResponse uploadImage(MultipartFile multipartFile) {
        final Image image = Image.of(imageDir, extractExtension(multipartFile));
        image.save(multipartFile);
        if (image.canNotCompress()) {
            return new ImageResponse(image.getName());
        }

        final String compressedImageName = image.compress();
        return new ImageResponse(compressedImageName);
    }

    private String extractExtension(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }
}
