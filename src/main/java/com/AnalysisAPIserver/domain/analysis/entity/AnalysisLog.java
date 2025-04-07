package com.AnalysisAPIserver.domain.analysis.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_logs")
@Data
@NoArgsConstructor
public class AnalysisLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;
    private String result;
    private String clientId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public AnalysisLog(String query, String result, String clientId) {
        this.query = query;
        this.result = result;
        this.clientId = clientId;
    }
}
