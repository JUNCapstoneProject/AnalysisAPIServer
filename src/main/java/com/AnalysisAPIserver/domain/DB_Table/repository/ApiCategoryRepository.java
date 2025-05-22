package com.AnalysisAPIserver.domain.DB_Table.repository;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiCategory;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ApiCategoryRepository
        extends JpaRepository<ApiCategory, Long> {
}
