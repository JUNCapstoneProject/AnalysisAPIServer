//package com.AnalysisAPIserver.config;
//
//
//import com.AnalysisAPIserver.common.Interceptor.AuthInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * WebConfig.java.
// * Spring MVC 설정을 위한 클래스입니다.
// * AuthInterceptor를 등록하여 API 요청에 대한 인증을 처리합니다.
// */
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//    /**
//     * 인증 인터셉터.
//     */
//    private final AuthInterceptor authInterceptor;
//
//    /**
//     * 인터셉터 등록을 위한 메서드입니다.
//     * 확장 시 기존 URL 패턴이 변경될 수 있으므로 주의가 필요합니다.
//     *
//     * @param registry 인터셉터 레지스트리
//     */
//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/api/**");
//    }
//}
