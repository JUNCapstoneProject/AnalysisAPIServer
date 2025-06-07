package com.AnalysisAPIserver.domain.DB_Table.repository;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    /**
     * 클라이언트 ID로 애플리케이션을 조회합니다.
     * @param clientId 클라이언트 ID.
     * @return 해당 클라이언트 ID를 가진 애플리케이션의 Optional.
     */
    Optional<Application> findByClientId(String clientId);

    /**
     * 특정 소유자의 모든 애플리케이션 목록을 조회합니다.
     * @param owner 애플리케이션 소유자 엔티티.
     * @return 해당 소유자의 애플리케이션 목록.
     */
    List<Application> findAllByOwner(ApiUser owner);

    /**
     * 주어진 클라이언트 ID 목록에 해당하는 모든 애플리케이션 목록을 조회합니다.
     * @param clientIds 클라이언트 ID 목록.
     * @return 주어진 클라이언트 ID 목록에 해당하는 애플리케이션 목록.
     */
    List<Application> findAllByClientIdIn(List<String> clientIds);

    /**
     * 클라이언트 ID와 클라이언트 Secret으로 애플리케이션 정보를 조회합니다.
     * @param clientId 클라이언트 ID
     * @param clientSecret 클라이언트 Secret
     * @return Optional<Application>
     */
    Optional<Application> findByClientIdAndClientSecret(String clientId,
                                                        String clientSecret);

}
