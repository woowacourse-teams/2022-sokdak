package com.wooteco.sokdak.image.dto;

public class ImageResponse {

    private final String imageName;

    public ImageResponse(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
