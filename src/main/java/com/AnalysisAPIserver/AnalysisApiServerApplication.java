package com.AnalysisAPIserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Analysis API 서버의 메인 애플리케이션 클래스이다.
 */
@SpringBootApplication
public final class AnalysisApiServerApplication {

    private AnalysisApiServerApplication() {
        // SpringBootApplication이므로 인스턴스 생성 금지
    }

    /**
     * 메인 메소드. Spring Boot 애플리케이션을 실행한다.
     *
     * @param args 프로그램 실행 인자
     */
    public static void main(final String[] args) {
        SpringApplication.run(AnalysisApiServerApplication.class, args);
    }
}
