package com.example.full_party.slice_test.api_layer;

import com.full_party.user.dto.UserPostDto;
import com.full_party.values.Gender;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

//    @Test
//    public void postUserTest() {
//        UserPostDto userPostDto = new UserPostDto(
//                new UserInfo(
//                        "gilbit1030@gmail.com",
//                        "0hyunCho",
//                        "1030",
//                        "example.jpg",
//                        new Date("1992.10.30"),
//                        Gender.MALE,
//                        "경기도 수원시 장안구 대평로 27 화서파크푸르지오 104동 1401호",
//                        "010-9512-8646"
//                )
//        );
//
//        String content = gson.toJson(userPostDto);
//
//        try {
//            ResultActions resultActions = mockMvc.perform(
//                    post("v1/users")
//                            .accept(MediaType.APPLICATION_JSON)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(content)
//            );
//
//            resultActions.andExpect(status().isCreated());
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    public void getUserTest() {
//
//    }
//
//    @Test
//    public void getUserDetailsTest() {
//
//    }
//
//    @Test
//    public void patchUserTest() {
//
//    }
//
//    @Test
//    public void deleteUserTest() {
//
//    }
}
