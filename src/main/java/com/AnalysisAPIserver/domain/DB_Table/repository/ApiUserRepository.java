package com.AnalysisAPIserver.domain.DB_Table.repository;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 클라이언트 리포지토리이다.
 */
@Repository
public interface ApiUserRepository
        extends JpaRepository<ApiUser, Long> {

    /**
     * 개발자 ID로 사용자 엔티티를 조회합니다.
     * @param developerId 개발자 ID.
     * @return 해당 개발자 ID를 가진 사용자의 Optional.
     */
    Optional<com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser> findById(
            Long developerId); // LineLength 오류 해결을 위해 줄바꿈

    /**
     * 사용자 이름으로 사용자 엔티티를 조회합니다.
     * @param userName 사용자 이름.
     * @return 해당 사용자 이름을 가진 사용자의 Optional.
     */
    Optional<com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser>
    findByUserName(
            String userName); // LineLength 오류 해결을 위해 줄바꿈

}
