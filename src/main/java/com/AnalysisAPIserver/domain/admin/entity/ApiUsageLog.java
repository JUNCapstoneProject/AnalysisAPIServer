package com.AnalysisAPIserver.domain.admin.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_usage_logs")
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientId; // API 요청을 보낸 클라이언트 ID

    @Column(nullable = false)
    private String endpoint; // 호출한 API 엔드포인트

    @Column(nullable = false)
    private LocalDateTime requestTime; // 요청 시간
}
