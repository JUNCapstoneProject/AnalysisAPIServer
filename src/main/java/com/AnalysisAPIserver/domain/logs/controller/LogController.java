//package com.AnalysisAPIserver.domain.logs.controller;
//
//import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
//import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
//import com.AnalysisAPIserver.domain.auth.exception.ResourceNotFoundException;
//import com.AnalysisAPIserver.domain.auth.jwt.JwtTokenProvider;
//import com.AnalysisAPIserver.domain.logs.dto.DeveloperApiStatisticsResponse;
//import com.AnalysisAPIserver.domain.logs.service.LogService;
//import io.jsonwebtoken.JwtException;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * API 사용 통계 관련 요청을 처리하는 컨트롤러입니다.
// */
//@RestController
//@RequestMapping("/api/statistics")
//@RequiredArgsConstructor
//public class LogController {
//
//    private static final Logger log
//    = LoggerFactory.getLogger(LogController.class);
//    private final LogService logService;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final ApiUserRepository apiUserRepository;
//
//    /**
//     * 특정 개발자의 API 통계 정보를 조회합니다.
//     * 요청자는 자신의 통계 정보만 조회할 수 있습니다.
//     *
//     * @param pathDeveloperId     통계를 조회할 개발자의 ID (URL 경로에서 받음).
//     * @param authorizationHeader 인증 토큰 ("Bearer " 접두사 포함).
//     * @return API 통계 응답 또는 오류 메시지를 담은 ResponseEntity.
//     */
//    @GetMapping("/{developerId}")
//    public ResponseEntity<?> getDeveloperStatistics(
//            @PathVariable("developerId") final Long pathDeveloperId,
//            @RequestHeader
//            ("Authorization") final String authorizationHeader) {
//
//        try {
//            String accessToken = null;
//            String prefix = "Bearer ";
//            if (StringUtils.hasText(authorizationHeader)
//                    && authorizationHeader.startsWith(prefix)) {
//                accessToken = authorizationHeader.substring(prefix.length());
//            }
//
//            if (!StringUtils.hasText(accessToken)) {
//                log.warn("getDeveloperStatistics: Missing or empty token "
//                        + "for pathDeveloperId {}", pathDeveloperId);
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(createErrorResponse("인증 토큰이 제공되지 않았습니다."));
//            }
//
//            if (!jwtTokenProvider.validateToken(accessToken)) {
//                log.warn("getDeveloperStatistics: Invalid token "
//                        + "for pathDeveloperId {}", pathDeveloperId);
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(createErrorResponse("유효하지 않은 인증 토큰입니다."));
//            }
//
//            String tokenUserName = jwtTokenProvider.getUserName(accessToken);
//            if (!StringUtils.hasText(tokenUserName)) {
//                log.warn("getDeveloperStatistics: "
//                + "UserName not found in token "
//                        + "for pathDeveloperId {}", pathDeveloperId);
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body
//                        (createErrorResponse("토큰에서 사용자 이름을 추출할 수 없습니다."));
//            }
//
//            Optional<ApiUser> tokenOwnerOpt =
//                    apiUserRepository.findByUserName(tokenUserName);
//            if (tokenOwnerOpt.isEmpty()) {
//                log.warn("getDeveloperStatistics: User '{}' (from token) "
//                        + "not found in DB. pathDeveloperId: {}",
//                        tokenUserName, pathDeveloperId);
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(createErrorResponse("해당 토큰의 사용자를 찾을 수 없습니다."));
//            }
//            Long tokenOwnerId = tokenOwnerOpt.get().getDeveloperId();
//
//            if (!tokenOwnerId.equals(pathDeveloperId)) {
//                log.warn("getDeveloperStatistics: Forbidden attempt. "
//                        + "Token owner ID {} (userName: {}) "
//                        + "tried to access stats for developer ID {}",
//                        tokenOwnerId, tokenUserName, pathDeveloperId);
//                return ResponseEntity
//                        .status(HttpStatus.FORBIDDEN)
//                        .body(createErrorResponse("자신의 통계 정보만 조회할 수 있습니다."));
//            }
//
//            DeveloperApiStatisticsResponse statisticsResponse =
//                    logService.getDeveloperStatistics(pathDeveloperId);
//
//            return ResponseEntity.ok(statisticsResponse);
//
//        } catch (ResourceNotFoundException e) {
//            log.warn("getDeveloperStatistics: Resource not found for "
//                    + "pathDeveloperId {}. Error: {}",
//                    pathDeveloperId, e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(createErrorResponse(e.getMessage()));
//        } catch (JwtException e) {
//            log.warn("getDeveloperStatistics: JWT processing error for "
//                    + "pathDeveloperId {}. Error: {}",
//                    pathDeveloperId, e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(createErrorResponse("토큰 처리 중 오류가 발생했습니다: "
//                            + e.getMessage()));
//        } catch (Exception e) {
//            log.error("getDeveloperStatistics: Unexpected error for "
//                    + "pathDeveloperId {}. Error: {}",
//                    pathDeveloperId, e.getMessage(), e);
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(createErrorResponse("통계 조회 중 서버 오류가 발생했습니다."));
//        }
//    }
//
//    /**
//     * 일관된 오류 응답 본문을 생성하기 위한 헬퍼 메소드입니다.
//     *
//     * @param message 오류 메시지 문자열입니다.
//     * @return 오류 메시지를 "error" 키로 포함하는
//     * {@code Map<String, String>} 객체입니다.
//     */
//    private Map<String, String> createErrorResponse(final String message) {
//        return Collections.singletonMap("error", message);
//    }
//}
