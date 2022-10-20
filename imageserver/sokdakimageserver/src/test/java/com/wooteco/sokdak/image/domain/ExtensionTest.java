package com.wooteco.sokdak.image.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.image.exception.NotSupportedExtensionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ExtensionTest {

    @DisplayName("jpg, jpeg, png, gif 확장자는 허용되는 확장자이다.")
    @ParameterizedTest
    @ValueSource(strings = {"jpg", "jpeg", "png", "gif"})
    void from(String value) {
        assertThatNoException().isThrownBy(() -> Extension.from(value));
    }

    @DisplayName("jpg, jpeg, png, gif 외의 확장자는 허용되지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"heic", "webp", "bmp", "tiff", "pdf", "svg", "ico"})
    void from_Exception(String value) {
        assertThatThrownBy(() -> Extension.from(value))
                .isInstanceOf(NotSupportedExtensionException.class);
    }
}
