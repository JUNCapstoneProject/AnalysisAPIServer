package com.AnalysisAPIserver.domain.DB_Table.repository;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiStatics;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiStaticsRepository
        extends JpaRepository<ApiStatics, Long> {

    /**
     * 특정 개발자의 API 통계 목록을 조회합니다.
     * @param developer 개발자 엔티티.
     * @return 해당 개발자의 API 통계 목록.
     */
    List<ApiStatics> findByDeveloper(ApiUser developer);

    /**
     * 특정 애플리케이션의 API 통계 목록을 조회합니다.
     * @param application 애플리케이션 엔티티.
     * @return 해당 애플리케이션의 API 통계 목록.
     */
    List<ApiStatics> findByApplication(Application application);

}
