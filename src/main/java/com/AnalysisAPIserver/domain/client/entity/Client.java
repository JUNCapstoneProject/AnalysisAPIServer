package com.AnalysisAPIserver.domain.client.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long developerId;  // 개발자 ID (FK)

    @Column(unique = true, nullable = false)
    private String clientId;

    private String name;
}
