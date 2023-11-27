package com.full_party.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {


    private String email;
    private Integer code;

    public MailDto(Integer code) {
        this.email = "";
        this.code = code;
    }
}
