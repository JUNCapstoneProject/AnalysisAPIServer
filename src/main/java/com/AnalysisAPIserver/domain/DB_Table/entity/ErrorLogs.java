package com.AnalysisAPIserver.domain.DB_Table.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ErrorLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLogs {

    /**
     * 요청 ID (ApiStatics와 동일).
     */
    @Id
    @Column(name = "request_id")
    private Long requestId;

    /**
     * 연관된 API 통계 엔티티.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "request_id")
    private ApiStatics apiStatics;

    /**
     * 에러 메시지.
     */
    @Lob
    @Column(name = "err_message", nullable = false)
    private String errMessage;
}
