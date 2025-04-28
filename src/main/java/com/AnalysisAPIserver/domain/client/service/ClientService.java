package com.AnalysisAPIserver.domain.client.service;

import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.entity.Client;
import com.AnalysisAPIserver.domain.client.exception.ClientErrorCode;
import com.AnalysisAPIserver.domain.client.exception.ClientException;
import com.AnalysisAPIserver.domain.client.repository.ClientRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 클라이언트 서비스이다.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    /**
     * 클라이언트 리포지토리.
     */
    private final ClientRepository clientRepository;

    /**
     * 클라이언트를 생성한다.
     *
     * @param request 생성 요청
     * @return 클라이언트 응답
     */
    @Transactional
    public ClientResponse createClient(final ClientCreateRequest request) {
        String generatedClientId = UUID.randomUUID().toString();
        Client client = Client.builder()
                .developerId(request.getDeveloperId())
                .clientId(generatedClientId)
                .name(request.getName())
                .build();
        this.clientRepository.save(client);
        return new ClientResponse(client);
    }

    /**
     * 개발자 ID로 클라이언트 리스트 조회.
     *
     * @param developerId 개발자 ID
     * @return 클라이언트 응답 리스트
     */
    @Transactional(readOnly = true)
    public List<ClientResponse> getClientsByDeveloper(final Long developerId) {
        return this.clientRepository.findByDeveloperId(developerId).stream()
                .map(ClientResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 클라이언트 삭제 메소드.
     *
     * @param clientId 클라이언트 ID
     */
    @Transactional
    public void deleteClient(final String clientId) {
        Client client = this.clientRepository.findByClientId(clientId)
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode.CLIENT_NOT_FOUND));
        this.clientRepository.delete(client);
    }
}
