package com.AnalysisAPIserver.domain.client.service;



import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.entity.Client;
import com.AnalysisAPIserver.domain.client.exception.ClientErrorCode;
import com.AnalysisAPIserver.domain.client.exception.ClientException;
import com.AnalysisAPIserver.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public ClientResponse createClient(ClientCreateRequest request) {
        String clientId = UUID.randomUUID().toString();
        Client client = Client.builder()
                .developerId(request.getDeveloperId())
                .clientId(clientId)
                .name(request.getName())
                .build();
        clientRepository.save(client);
        return new ClientResponse(client);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> getClientsByDeveloper(Long developerId) {
        List<Client> clients = clientRepository.findByDeveloperId(developerId);
        return clients.stream()
                .map(ClientResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteClient(String clientId) {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientException(ClientErrorCode.CLIENT_NOT_FOUND));
        clientRepository.delete(client);
    }
}
