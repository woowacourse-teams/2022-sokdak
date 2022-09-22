package com.wooteco.sokdak.image;

import static com.wooteco.sokdak.image.ImageController.IMAGE_DIR;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public ImageResponse uploadImage(MultipartFile image) throws IOException {
        final String extension = image.getContentType().split("/")[1];
        final String imageName = UUID.randomUUID() + "." + extension;

        final File file = new File(IMAGE_DIR + imageName);
        image.transferTo(file);

        return new ImageResponse(imageName);
    }
}
