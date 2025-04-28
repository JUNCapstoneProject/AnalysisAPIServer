package com.AnalysisAPIserver.domain.client.controller;

import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.service.ClientService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 클라이언트 관련 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public final class ClientController {

    /**
     * 클라이언트 서비스.
     */
    private final ClientService clientService;

    /**
     * 클라이언트를 생성한다.
     *
     * @param request 클라이언트 생성 요청
     * @return 생성된 클라이언트 응답
     */
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @RequestBody final ClientCreateRequest request) {
        return ResponseEntity.ok(this.clientService.createClient(request));
    }

    /**
     * 개발자 ID로 클라이언트 목록을 조회한다.
     *
     * @param developerId 개발자 ID
     * @return 클라이언트 리스트
     */
    @GetMapping("/{developerId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDeveloper(
            @PathVariable final Long developerId) {
        return ResponseEntity.ok(
                this.clientService.getClientsByDeveloper(developerId)
        );
    }

    /**
     * 클라이언트를 삭제한다.
     *
     * @param clientId 클라이언트 ID
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteClient(
            @PathVariable final String clientId) {
        this.clientService.deleteClient(clientId);
        return ResponseEntity.ok("클라이언트 삭제 완료");
    }
}
