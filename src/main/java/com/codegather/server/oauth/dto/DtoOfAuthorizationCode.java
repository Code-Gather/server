package com.codegather.server.oauth.dto;

public record DtoOfAuthorizationCode(
        String oAuthServer,
        String authorizationCode
) {
}
