package com.AnalysisAPIserver.domain.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자 엔티티이다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public final class User {

    /**
     * 사용자 ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이메일.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 사용자 비밀번호.
     */
    @Column(nullable = false)
    private String password;

    /**
     * 연결된 API 키.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ApiKey apiKey;

    /**
     * 생성자.
     *
     * @param emailParam 이메일
     * @param passwordParam 비밀번호
     */
    public User(final String emailParam, final String passwordParam) {
        this.email = emailParam;
        this.password = passwordParam;
    }
}
