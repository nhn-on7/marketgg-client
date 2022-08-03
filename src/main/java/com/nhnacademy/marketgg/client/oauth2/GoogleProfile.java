package com.nhnacademy.marketgg.client.oauth2;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class GoogleProfile implements OauthProfile {

    private String email;
    private String name;
    private String provider;

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

}
