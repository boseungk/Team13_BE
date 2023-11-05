package com.theocean.fundering.global.oauth2.userInfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    protected OAuth2UserInfo(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract Long getId();

    public abstract String getNickname();

    public abstract String getImageUrl();

    public abstract String getEmail();
}