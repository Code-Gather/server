package com.codegather.authserver.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperty {
    private Map<String, OAuthInfo> properties;

    @Getter
    @Setter
    public static class OAuthInfo {
        private String clientId;
        private String clientSecret;
        private String tokenUri;
        private String userInfoUri;
        private String redirectUri;
    }
}
