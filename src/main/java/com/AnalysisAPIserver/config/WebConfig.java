//package com.AnalysisAPIserver.config;
//
//import com.AnalysisAPIserver.common.Interceptor.AuthInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import java.util.Arrays;
//import java.util.List;
//
///**
//
//        * Spring MVC 설정을 위한 클래스입니다.
//
//  * {@link AuthInterceptor}를 등록하여 특정 API 경로에 대한 요청을 가로채
//
//  * 필요한 전처리 및 후처리를 수행합니다.
//    \*/
//@Configuration
//@RequiredArgsConstructor
//public final class WebConfig implements WebMvcConfigurer { // 클래스를 final로 선언
//
//    private static final Logger log
//    = LoggerFactory.getLogger(WebConfig.class);
//    private final AuthInterceptor authInterceptor;
//
//    /**
//
//            * 애플리케이션의 인터셉터 설정을 구성합니다.
//
//            * {@link AuthInterceptor}를 특정 경로 패턴에 적용하고,
//
//            * 일부 경로는 인터셉터 적용 대상에서 제외합니다.
//
//            *
//            * @param registry 인터셉터 설정을 등록하기 위한 객체입니다.
//        \*/
//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        List<String> excludePatterns = Arrays.asList(
//                "/api/auth/**",       // auth 패키지 전체 API 제외
//                "/api/analysis/**",   // analysis 패키지 전체 API 제외
//                "/api/client",        // 정확히 "/api/client" 경로만 제외
//                // Swagger UI 및 API docs 관련 경로 제외
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/api-docs/**"
//        );
//
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/api/**") // 기본적으로 /api/** 아래 모든 경로에 적용
//                .excludePathPatterns(excludePatterns); // 명시된 경로 및 패턴들은 제외
//
//        log.info("AuthInterceptor registered for path pattern /api/**");
//        log.info("AuthInterceptor excluded paths/patterns: {}",
//        excludePatterns);
//    }
//}
