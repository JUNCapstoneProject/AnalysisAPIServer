package com.AnalysisAPIserver.domain.client.controller;


import com.AnalysisAPIserver.domain.client.dto.ClientApiError;
import com.AnalysisAPIserver.domain.client.dto.ClientApiResponse;
import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientCreateResponse;
import com.AnalysisAPIserver.domain.client.dto.ClientDetailResponseDto;
import com.AnalysisAPIserver.domain.client.dto.ClientListResponseDto;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.dto.ClientUpdateNameRequest;
import com.AnalysisAPIserver.domain.client.exception.ClientException;
import com.AnalysisAPIserver.domain.client.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 클라이언트 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 클라이언트 생성, 조회, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public final class ClientController {

    /**
     * 클라이언트 관련 비즈니스 로직을 처리하는 서비스입니다.
     */
    private final ClientService clientService;

    /**
     * 새 클라이언트를 생성합니다.
     *
     * @param request 클라이언트 생성 요청 정보를 담은 DTO
     * @return 생성된 클라이언트 정보와 API 응답 객체
     */
    @PostMapping
    public ResponseEntity<ClientApiResponse<ClientCreateResponse>> createClient(
            @RequestBody @Valid final ClientCreateRequest request) {
        ClientCreateResponse response = clientService.createClient(request);
        return ResponseEntity.ok(ClientApiResponse.success(response));
    }

    /**
     * 특정 개발자가 소유한 클라이언트 목록을 상세 정보와 함께 조회합니다.
     *
     * @param developerId 개발자 ID
     * @return 해당 개발자의 클라이언트 목록과 API 응답 객체
     */
    @GetMapping("/developer/{developerId}")
    public ResponseEntity
            <ClientApiResponse<List<ClientListResponseDto>>> getClientsByOwner(
            @PathVariable("developerId") final Long developerId) {
        List<ClientListResponseDto> clients =
                clientService.getDetailedClientsByOwner(developerId);
        return ResponseEntity
                .ok(ClientApiResponse.success(clients));
    }

    /**
     * 특정 클라이언트 ID에 해당하는 클라이언트의 상세 정보를 조회합니다.
     *
     * @param clientId 조회할 클라이언트의 ID
     * @return 해당 클라이언트의 상세 정보와 API 응답 객체
     */
    @GetMapping("/detail/{clientId}")
    public ResponseEntity
            <ClientApiResponse<ClientDetailResponseDto>> getClientDetail(
            @PathVariable final String clientId) {
        ClientDetailResponseDto dto = clientService.getClientDetail(clientId);
        return ResponseEntity.ok(ClientApiResponse.success(dto));
    }

    /**
     * 특정 클라이언트의 이름을 수정합니다.
     *
     * @param clientId 수정할 클라이언트의 ID
     * @param request  새로운 클라이언트 이름을 담은 DTO
     * @return 수정된 클라이언트 정보와 API 응답 객체
     */
    @PutMapping("/{clientId}")
    public ResponseEntity<ClientApiResponse<ClientResponse>> updateClientName(
            @PathVariable final String clientId,
            @RequestBody @Valid final ClientUpdateNameRequest request) {
        ClientResponse response =
                clientService.updateClientName(clientId, request.getNewName());
        return ResponseEntity.ok(ClientApiResponse.success(response));
    }

    /**
     * 여러 클라이언트를 한 번에 삭제합니다.
     *
     * @param clientIds 삭제할 클라이언트 ID 목록
     * @return 작업 성공 여부를 담은 API 응답 객체. 실패 시 에러 메시지를 포함합니다.
     */
    @PostMapping("/delete")
    public ResponseEntity<ClientApiResponse<Void>> deleteClients(
            @RequestBody final List<String> clientIds) {
        try {
            clientService.deleteClients(clientIds);
            return ResponseEntity.ok(ClientApiResponse.success(null));
        } catch (ClientException e) {
            ClientApiError error = new ClientApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getErrorCode().name(),
                    e.getMessage()
            );
            return ResponseEntity
                    .badRequest().body(ClientApiResponse.error(error));
        }
    }
}
