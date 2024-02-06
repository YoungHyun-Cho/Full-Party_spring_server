package com.full_party.user;

import com.full_party.domain.user.entity.User;
import com.full_party.domain.user.repository.UserRepository;
import com.full_party.domain.user.service.UserService;
import com.full_party.global.values.Coordinates;
import com.full_party.global.values.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void createUserTest() {

        User user = new User();
        user.setEmail("0hyuncho@gmail.com");
        user.setUserName("조영현");
        user.setProfileImage("defaultProfile.png");
        user.setPassword("test123!");
        user.setBirth(new Date(92, 9, 30));
        user.setGender(Gender.MALE);
        user.setMobile("010-1234-1234");
        user.setAddress("경기도 수원시 장안구 대평로 111");
        user.setCoordinates(new Coordinates(37.496562, 127.024761));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

}
