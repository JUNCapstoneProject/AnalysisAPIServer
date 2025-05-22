package com.AnalysisAPIserver.domain.DB_Table.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "AppCategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppCategory {

    /**
     * 애플리케이션 카테고리 ID.
     */
    @Id
    @Column(name = "app_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appCategoryId;

    /**
     * 애플리케이션 카테고리 이름.
     */
    @Column(name = "app_category_name", nullable = false)
    private String appCategoryName;

    /**
     * 이 카테고리에 속하는 애플리케이션 목록.
     */
    @OneToMany(mappedBy = "appCategory")
    private List<Application> applications;
}
