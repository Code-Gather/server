package com.codegather.server.oauth;

import com.codegather.server.oauth.dto.DtoOfGitHubUserInfo;
import com.codegather.server.oauth.dto.DtoOfOAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperty oAuthProperty;
    private final ObjectMapper objectMapper;

    public Mono<DtoOfGitHubUserInfo> get(String target, String code) {
        OAuthProperty.OAuthInfo property = oAuthProperty.properties().get(target);
        return getOAuthToken(property, code)
                .flatMap(response -> getUserInfo(property, response.accessToken()));
    }

    private Mono<DtoOfGitHubUserInfo> getUserInfo(OAuthProperty.OAuthInfo property, String accessToken) {
        return WebClient.builder().baseUrl(property.userInfoUri()).build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(DtoOfGitHubUserInfo.class);
    }

    private Mono<DtoOfOAuthToken> getOAuthToken(OAuthProperty.OAuthInfo property, String code) {
        return WebClient.builder().baseUrl(property.tokenUri()).build()
                .post()
                .uri(setQueryParams(property, code))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::validationGetOAuthTokenBody);
    }

    private Function<UriBuilder, URI> setQueryParams(OAuthProperty.OAuthInfo property, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", property.clientId());
        params.add("client_secret", property.clientSecret());
        params.add("code", code);
        return uri -> uri.queryParams(params).build();
    }

    private Mono<DtoOfOAuthToken> validationGetOAuthTokenBody(String body) {
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            if (jsonNode.has("error")) {
                throw new RuntimeException("Error in response: " + jsonNode.get("error").asText());
            }
            return Mono.just(objectMapper.treeToValue(jsonNode, DtoOfOAuthToken.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Undefined Error. Failed to process response body.", e);
        }
    }
}
