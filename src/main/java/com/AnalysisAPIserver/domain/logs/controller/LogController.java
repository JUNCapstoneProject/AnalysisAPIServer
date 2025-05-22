package com.AnalysisAPIserver.domain.logs.controller;


import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import com.AnalysisAPIserver.domain.auth.exception.UnauthorizedException;
import com.AnalysisAPIserver.domain.auth.jwt.JwtTokenProvider;
import com.AnalysisAPIserver.domain.logs.dto.DeveloperApiStatisticsResponse;
import com.AnalysisAPIserver.domain.logs.service.LogService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping; // Specific import
import org.springframework.web.bind.annotation.PathVariable; // Specific import
import org.springframework.web.bind.annotation.RequestHeader; // Specific import
import org.springframework.web.bind.annotation.RequestMapping; // Specific import
import org.springframework.web.bind.annotation.RestController; // Specific import


@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class LogController {

    /**
     * 로그 관련 비즈니스 로직을 처리하는 서비스입니다.
     */
    private final LogService logService;
    /**
     * JWT 토큰 생성, 검증 및 클레임 추출을 담당하는 클래스입니다.
     */
    private final JwtTokenProvider jwtTokenProvider;
    /**
     * 애플리케이션 정보에 접근하기 위한 Repository입니다.
     */
    private final ApplicationRepository applicationRepository;

    /**
     * 특정 개발자의 API 통계 정보를 조회합니다.
     * 요청자는 자신의 통계 정보만 조회할 수 있습니다.
     *
     * @param pathDeveloperId    통계를 조회할 개발자의 ID (URL 경로에서 받음).
     * @param authorizationHeader 인증 토큰 ("Bearer " 접두사 포함).
     * @param clientIdHeader      요청을 보낸 애플리케이션의 client_id (선택적 추가 검증).
     * @return API 통계 응답 또는 오류 메시지.
     */
    @GetMapping("/{developerId}")
    public ResponseEntity<?> getDeveloperStatistics(
            @PathVariable("developerId") final Long pathDeveloperId,
            @RequestHeader("Authorization") final String authorizationHeader,
            @RequestHeader(name = "client_id",
                    required = false) final String clientIdHeader) {

        try {
            // 1. Authorization 토큰 처리
            String accessToken = null;
            String prefix = "Bearer ";
            if (StringUtils.hasText(authorizationHeader)
                    && authorizationHeader.startsWith(prefix)) {
                accessToken = authorizationHeader.substring(prefix.length());
            }

            if (accessToken == null
                    || !jwtTokenProvider.validateToken(accessToken)) {
                throw new UnauthorizedException("유효하지 않은 인증 토큰입니다.");
            }

            if (StringUtils.hasText(clientIdHeader)) {
                Optional<Application> appOpt = applicationRepository
                        .findByClientId(clientIdHeader);
                if (appOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("제공된 client_id가 유효하지 않습니다.");
                }
            }

            DeveloperApiStatisticsResponse statisticsResponse =
                    logService.getDeveloperStatistics(pathDeveloperId);

            return ResponseEntity.ok(statisticsResponse);

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Consider logging the exception e here for better debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("통계 조회 중 서버 오류가 발생했습니다.");
        }
    }
}
