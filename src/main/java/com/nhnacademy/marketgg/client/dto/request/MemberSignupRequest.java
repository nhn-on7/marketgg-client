package com.nhnacademy.marketgg.client.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Getter
public class MemberSignupRequest {

    @Email
    @NotBlank
    private final String email;

    //'숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
    //(특수문자는 정의된 특수문자만 사용 가능)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$")
    private String password;

    @NotBlank
    private final String name;

    @NotBlank
    private final String phoneNumber;

    @NotNull
    private final Character gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate birthDate;

    private final String referrerEmail;

    @NotNull
    private final Integer zipcode;

    @NotBlank
    private final String address;

    @NotBlank
    private final String detailAddress;

    @NotNull
    private final Boolean inlineRadioOptions;

    private final String provider;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public MemberSignupToAuth getSignupRequestToAuth() {
        return new MemberSignupToAuth(this.email, this.password, this.name, this.phoneNumber, this.referrerEmail,
            this.provider);
    }

    public MemberSignupToShopMember getSignupRequestToShopMember(String uuid, String referrerUuid) {
        return new MemberSignupToShopMember(this.gender, Boolean.FALSE, this.birthDate, uuid, referrerUuid,
            this.zipcode, this.address, this.detailAddress);
    }

}
