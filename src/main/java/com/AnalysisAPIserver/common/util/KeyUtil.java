package com.AnalysisAPIserver.common.util;

import java.util.UUID;

public class KeyUtil {
    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
