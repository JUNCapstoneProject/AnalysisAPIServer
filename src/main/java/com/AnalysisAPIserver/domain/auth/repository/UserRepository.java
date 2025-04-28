package com.AnalysisAPIserver.domain.auth.repository;

import com.AnalysisAPIserver.domain.auth.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 사용자 리포지토리이다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 Optional
     */
    Optional<User> findByEmail(String email);
}
