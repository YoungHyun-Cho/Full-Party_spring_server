package com.full_party.auth.userdetails;

import com.full_party.auth.utils.CustomAuthorityUtils;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.exception.GlobalExceptionAdvice;
import com.full_party.user.entity.User;
import com.full_party.user.repository.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final PasswordEncoder passwordEncoder;

    public UserDetailService(UserRepository userRepository, CustomAuthorityUtils customAuthorityUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customAuthorityUtils = customAuthorityUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws BusinessLogicException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        User foundUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return new UserDetail(foundUser, customAuthorityUtils);
    }

    // 여기에서 ExceptionHandler 정의하고 응답 보내주어야 함.
    // DispatchServlet 도달 이전에 발생한 예외인 것 감안해야 함.

}
