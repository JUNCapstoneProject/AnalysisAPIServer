package com.AnalysisAPIserver.common.util;

import java.util.UUID;

/**
 * API 키를 생성하는 유틸 클래스이다.
 */
public final class KeyUtil {

    private KeyUtil() {
        // 생성자 막음
    }

    /**
     * API 키를 랜덤으로 생성한다.
     *
     * @return 랜덤 API 키
     */
    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
