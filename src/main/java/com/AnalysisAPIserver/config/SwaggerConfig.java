package com.AnalysisAPIserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 스웨거(Swagger) 설정하는 클래스이다.
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 빈 등록.
     *
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AnalysisAPIserver API")
                        .version("1.0")
                        .description("API 문서"));
    }
}
