package com.full_party.domain.auth.userdetails;

import com.full_party.domain.auth.utils.CustomAuthorityUtils;
import com.full_party.global.exception.BusinessLogicException;
import com.full_party.global.exception.ExceptionCode;
import com.full_party.domain.user.entity.User;
import com.full_party.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws BusinessLogicException {

        Optional<User> optionalUser = userRepository.findByEmail(username);
        User foundUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return new UserDetail(foundUser, customAuthorityUtils);
    }
}
