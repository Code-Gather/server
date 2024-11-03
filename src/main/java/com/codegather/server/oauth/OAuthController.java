package com.codegather.server.oauth;

import com.codegather.server.oauth.dto.DtoOfAuthorizationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping
    public Mono<ResponseEntity<?>> loginOAuth(@RequestBody DtoOfAuthorizationCode dto) {
        return oAuthService.get(dto.oAuthServer(), dto.authorizationCode())
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }
}
