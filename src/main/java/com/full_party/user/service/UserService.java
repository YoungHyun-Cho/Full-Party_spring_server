package com.full_party.user.service;

import com.full_party.auth.utils.CustomAuthorityUtils;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.user.entity.User;
import com.full_party.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils customAuthorityUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    public User createUser(User user) {

        verifyExistsEmail(user.getEmail());

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        List<String> roles = customAuthorityUtils.createRoles(user.getEmail());
        user.setRoles(roles);

        User newUser = new User(user);

        return userRepository.save(newUser);
    }

    public User findUser(Long userId) {
        return findVerifiedUser(userId);
    }

    public User findUser(String email) {
        return findVerifiedUser(email);
    }

    public User updateUser(User user) {
        User foundUser = findVerifiedUser(user.getId());
        User updatedUser = new User(foundUser, user);
        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long userId) {
        User user = findVerifiedUser(userId);
        userRepository.delete(user);
    }

    private User findVerifiedUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }

    private User findVerifiedUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }

    private void verifyExistsEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
    }

    public User updateExp(Long userId, Integer exp) {
        User foundUser = findUser(userId);

        foundUser.setExp(foundUser.getExp() + exp);

        return foundUser;
    }
}