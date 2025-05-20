package com.AnalysisAPIserver.common.Interceptor;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiCategory;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiStatics;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.ErrorLogs;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiStaticsRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ErrorLogsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * API 요청 시 인증 및 응답 후 로깅을 처리하는 Interceptor.
 *
 * <p>주의: 이 클래스는 HandlerInterceptor를 확장하여 재정의되므로,
 * 서브클래싱 시에는 preHandle, afterCompletion 메서드를 안전하게 오버라이드해야 합니다.</p>
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    /** Application 엔티티에 접근하기 위한 레포지토리. */
    private final ApplicationRepository applicationRepository;

    /** API 요청 통계를 저장하는 레포지토리. */
    private final ApiStaticsRepository apiStaticsRepository;

    /** 에러 로그를 저장하는 레포지토리. */
    private final ErrorLogsRepository errorLogsRepository;

    /**
     * API 요청 전에 실행되는 메서드. 인증을 처리함.
     *
     * @param request  HTTP 요청 객체.
     * @param response HTTP 응답 객체.
     * @param handler  처리 대상 핸들러.
     * @return 인증 성공 여부.
     * @throws Exception 예외 발생 시.
     */
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        String clientId = request.getHeader("client_id");

        if (clientId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API credentials");
            return false;
        }

        Optional<Application> app = applicationRepository
                .findByClientId(clientId);
        if (app.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API credentials");
            return false;
        }

        // 인증 통과한 앱 정보를 request에 저장하여 이후 로깅에 활용.
        request.setAttribute("app", app.get());
        return true;
    }

    /**
     * 요청 처리 후 실행되는 메서드. 요청/응답 결과를 로깅함.
     *
     * <p>주의: 예외가 존재하는 경우 에러 로그도 기록됩니다.</p>
     *
     * @param request  HTTP 요청 객체.
     * @param response HTTP 응답 객체.
     * @param handler  처리 대상 핸들러.
     * @param ex       발생한 예외 (있다면).
     * @throws Exception 예외 발생 시.
     */
    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex) throws Exception {

        Application app = (Application) request.getAttribute("app");
        if (app == null) {
            return; // 인증 실패한 요청은 무시.
        }

        // 통계 로깅을 위한 정보 추출
        ApiUser developer = app.getOwner();
        ApiCategory apiCategory = app.getApiCategory();
        int statusCode = response.getStatus();

        // ApiStatics 생성 및 저장.
        ApiStatics statics = ApiStatics.builder()
                .application(app)
                .developer(developer)
                .apiCategory(apiCategory)
                .statusCode(statusCode)
                .build();

        apiStaticsRepository.save(statics);

        // 예외가 존재할 경우 에러 로그도 함께 저장.
        if (ex != null) {
            ErrorLogs errorLogs = ErrorLogs.builder()
                    .apiStatics(statics)
                    .errMessage(ex.getMessage())
                    .build();

            errorLogsRepository.save(errorLogs);
        }
    }

    /**
     * Exception의 스택 트레이스를 문자열로 변환하는 메서드.
     *
     * @param ex 변환할 예외 객체.
     * @return 문자열로 변환된 스택 트레이스.
     */
    private String getStackTraceAsString(final Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
