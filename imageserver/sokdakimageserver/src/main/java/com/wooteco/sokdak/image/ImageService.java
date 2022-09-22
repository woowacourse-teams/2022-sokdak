package com.wooteco.sokdak.image;

import static com.wooteco.sokdak.image.ImageController.IMAGE_DIR;

import com.wooteco.sokdak.exception.FileIOException;
import java.io.File;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public ImageResponse uploadImage(MultipartFile image) {
        final String extension = image.getContentType().split("/")[1];
        final String imageName = UUID.randomUUID() + "." + extension;

        try {
            final File file = new File(IMAGE_DIR + imageName);
            image.transferTo(file);
        } catch (Exception e){
            throw new FileIOException();
        }

        return new ImageResponse(imageName);
    }
}
