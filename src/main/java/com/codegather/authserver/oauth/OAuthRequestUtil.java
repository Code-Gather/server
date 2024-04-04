package com.codegather.authserver.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OAuthRequestUtil {

    private final OAuthProperty oAuthProperty;

    public Mono<DtoOfGitHubUserInfo> get(String target, String code) {
        OAuthProperty.OAuthInfo property = oAuthProperty.getProperties().get(target);
        return getOAuthToken(property, code).flatMap(response -> getUserInfo(property, response.getAccessToken()));
    }

    private Mono<DtoOfGitHubUserInfo> getUserInfo(OAuthProperty.OAuthInfo property, String accessToken) {
        return WebClient.builder().baseUrl(property.getUserInfoUri()).build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(DtoOfGitHubUserInfo.class);
    }

    private Mono<DtoOfOAuthToken> getOAuthToken(OAuthProperty.OAuthInfo property, String code) {
        return WebClient.builder().baseUrl(property.getTokenUri()).build()
                .post()
                .uri(setQueryParams(property, code))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DtoOfOAuthToken.class);
    }

    private Function<UriBuilder, URI> setQueryParams(OAuthProperty.OAuthInfo property, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", property.getClientId());
        params.add("client_secret", property.getClientSecret());
        params.add("code", code);
        return uri -> uri.queryParams(params).build();
    }
}
