package com.wooteco.sokdak.image;

import java.util.Arrays;

public enum Extension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    ;

    private final String value;

    Extension(String value) {
        this.value = value;
    }

    public static Extension from(String extension) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(extension))
                .findAny()
                .orElseThrow(NotSupportedExtensionException::new);
    }

    public boolean canNotCompress() {
        return this == GIF;
    }

    public String getValue() {
        return value;
    }
}
