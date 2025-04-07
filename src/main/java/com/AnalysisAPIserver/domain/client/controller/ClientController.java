package com.AnalysisAPIserver.domain.client.controller;



import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("")
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientCreateRequest request) {
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping("/{developerId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDeveloper(@PathVariable Long developerId) {
        return ResponseEntity.ok(clientService.getClientsByDeveloper(developerId));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable String clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok("클라이언트 삭제 완료");
    }
}
