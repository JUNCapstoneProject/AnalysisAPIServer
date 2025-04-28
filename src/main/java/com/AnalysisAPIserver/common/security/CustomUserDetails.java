package com.AnalysisAPIserver.common.security;

import com.AnalysisAPIserver.domain.auth.entity.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 인증 정보를 담는 클래스이다.
 */
public final class CustomUserDetails implements UserDetails {

    /**
     * 유저 정보.
     */
    private final User user;

    /**
     * CustomUserDetails 생성자.
     *
     * @param userParam 유저 엔티티
     */
    public CustomUserDetails(final User userParam) {
        this.user = userParam;
    }

    /**
     * 유저 엔티티 반환.
     *
     * @return User 객체
     */
    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
