package com.AnalysisAPIserver.domain.DB_Table.repository;

import com.AnalysisAPIserver.domain.DB_Table.entity.AppCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppCategoryRepository
        extends JpaRepository<AppCategory, Long> {
}
