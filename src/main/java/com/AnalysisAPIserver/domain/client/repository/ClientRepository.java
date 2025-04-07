package com.AnalysisAPIserver.domain.client.repository;


import com.AnalysisAPIserver.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByDeveloperId(Long developerId);
    Optional<Client> findByClientId(String clientId);
}
