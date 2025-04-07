package com.AnalysisAPIserver.domain.logs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "api_logs")
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private String endpoint;
    private String method;
    private LocalDateTime timestamp;

    @Builder
    public ApiLog(String clientId, String endpoint, String method, LocalDateTime timestamp) {
        this.clientId = clientId;
        this.endpoint = endpoint;
        this.method = method;
        this.timestamp = timestamp;
    }
}
