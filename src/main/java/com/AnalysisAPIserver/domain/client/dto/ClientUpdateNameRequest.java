package com.AnalysisAPIserver.domain.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 클라이언트 이름 변경 요청 시 사용되는 DTO입니다.
 * 새로운 앱 이름을 포함합니다.
 */
@Getter
@NoArgsConstructor // Jackson 역직렬화를 위해 기본 생성자 추가
public class ClientUpdateNameRequest {
    /**
     * 변경할 새로운 클라이언트(애플리케이션)의 이름입니다.
     * 공백일 수 없습니다.
     */
    @NotBlank(message = "앱 이름은 비어 있을 수 없습니다.")
    private String newName;

    // 테스트 또는 특정 상황을 위해 모든 필드를 받는 생성자가 필요할 수 있으나,
    // 현재는 newName만 있으므로 @NoArgsConstructor와 setter 또는 @AllArgsConstructor로 충분.
    // 명시적으로 생성자를 추가한다면:
    // public ClientUpdateNameRequest(String newName) {
    // this.newName = newName;
    // }
}
