package com.AnalysisAPIserver.domain.logs.controller;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiStatics;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiStaticsRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class LogController {

    /**
     * 애플리케이션 정보에 접근하기 위한 리포지토리.
     */
    private final ApplicationRepository applicationRepository;
    /**
     * API 통계 정보에 접근하기 위한 리포지토리.
     */
    private final ApiStaticsRepository apiStaticsRepository;

    /**
     * 특정 개발자의 API 통계 정보를 조회합니다.
     * 이 메소드는 확장될 수 있지만, 현재 로직은 유지되어야 합니다.
     *
     * @param developerId 통계를 조회할 개발자 ID.
     * @param request HTTP 요청 객체.
     * @return API 통계 응답 또는 오류 메시지.
     */
    @GetMapping("/{developerId}")
    public ResponseEntity<?> getDeveloperStatistics(
            @PathVariable final Long developerId, // FinalParameters 적용
            final HttpServletRequest request) { // FinalParameters 적용
        String clientId = request.getHeader("client_id");


        Optional<Application> appOpt = applicationRepository.findByClientId(
                clientId); // LineLength 해결
        if (appOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Invalid client_id"); // LineLength 해결
        }


        ApiUser developer = appOpt.get().getOwner();


        List<ApiStatics> staticsList = apiStaticsRepository.findByDeveloper(
                developer); // LineLength 해결

        return ResponseEntity.ok(staticsList);
    }
}
