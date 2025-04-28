package com.AnalysisAPIserver.common.security;

import com.AnalysisAPIserver.domain.auth.entity.User;
import com.AnalysisAPIserver.domain.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 서비스를 제공하는 클래스이다.
 */
@Service
public final class CustomUserDetailsService implements UserDetailsService {

    /**
     * 사용자 리포지토리.
     */
    private final UserRepository userRepository;

    /**
     * CustomUserDetailsService 생성자.
     *
     * @param userRepositoryParam 사용자 리포지토리
     */
    public CustomUserDetailsService(final UserRepository userRepositoryParam) {
        this.userRepository = userRepositoryParam;
    }

    /**
     * 이메일을 통해 사용자 정보를 로드한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "사용자 없음: " + email));
        return new CustomUserDetails(user);
    }
}
