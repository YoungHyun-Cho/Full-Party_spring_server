package com.full_party.domain.user.service;

import com.full_party.domain.user.entity.User;
import com.full_party.domain.user.repository.UserRepository;
import com.full_party.global.exception.BusinessLogicException;
import com.full_party.global.exception.ExceptionCode;
import com.full_party.global.values.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(new User(user));
    }

    public User findUser(Long userId) {
        return setTransientValues(findVerifiedUser(userId));
    }

    public User findUser(String email) {
        return setTransientValues(findVerifiedUser(email));
    }

    private User setTransientValues(User user) {

        user.setLevelUpExp(Level.getLevel(user.getLevel()).getLevelUpExp());

        return user;
    }

    public User updateUser(User user) {

        User foundUser = findVerifiedUser(user.getId());
        User updatedUser = new User(foundUser, user);
        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long userId) {
        User user = findVerifiedUser(userId);
        deleteUser(user);
    }

    public void deleteUser(User user) {
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

    public void verifyExistsEmail(String email) throws BusinessLogicException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
    }

    public void verifyExistsUserName(String userName) throws BusinessLogicException {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
    }

    public Level.Result updateExp(Long userId, Integer exp) {

        User foundUser = findUser(userId);

        Level.Result result = Level.calculateLevel(foundUser.getExp() + exp, foundUser.getLevel());

        foundUser.setLevel(result.getLevel());
        foundUser.setExp(result.getExp());

        userRepository.save(foundUser);

        return result;
    }
}