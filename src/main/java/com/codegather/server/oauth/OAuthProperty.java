package com.codegather.server.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "oauth")
public record OAuthProperty(
        Map<String, OAuthInfo> properties
) {
    public record OAuthInfo(
            String clientId,
            String clientSecret,
            String tokenUri,
            String userInfoUri,
            String redirectUri
    ) {
    }
}
