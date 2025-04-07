package com.AnalysisAPIserver.domain.admin.dto;




import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiUsageResponse {
    private long totalRequests;
    private long uniqueUsers;
    private long avgRequestsPerUser;
}
