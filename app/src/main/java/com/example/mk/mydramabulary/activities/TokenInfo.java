package com.example.mk.mydramabulary.activities;

/**
 * Created by mk on 2017-02-28.
 */

public class TokenInfo {
    String access_token;
    String expires_in;
    String refresh_in;
    String token_type;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_in() {
        return refresh_in;
    }

    public void setRefresh_in(String refresh_in) {
        this.refresh_in = refresh_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
