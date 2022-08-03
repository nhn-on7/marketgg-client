package com.nhnacademy.marketgg.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberSignupToAuth {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String referrerEmail;
    private String provider;

}
