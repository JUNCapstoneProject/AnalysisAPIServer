package com.AnalysisAPIserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정하는 클래스이다.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈을 등록한다.
     *
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
