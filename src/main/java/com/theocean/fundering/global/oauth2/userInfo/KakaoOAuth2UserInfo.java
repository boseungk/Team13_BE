package com.theocean.fundering.global.oauth2.userInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public Long getId() {
        return Long.valueOf(attributes.get("id").toString());
    }

    @Override
    public String getNickname() {
        final Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        final Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (null == account || null == profile) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getImageUrl() {
        final Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        final Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (null == account || null == profile) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }

    @Override
    public String getEmail() {
        final Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        final Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (null == account || null == profile) {
            return null;
        }

        return (String) account.get("email");
    }


}