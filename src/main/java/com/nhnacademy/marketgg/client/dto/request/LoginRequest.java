package com.nhnacademy.marketgg.client.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Getter
public class LoginRequest {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private String password;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
