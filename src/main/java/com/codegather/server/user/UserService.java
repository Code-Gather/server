package com.codegather.server.user;

import com.codegather.server.oauth.OAuthService;
import com.codegather.server.user.dto.DtoOfLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;

    public Mono<User> login(DtoOfLogin loginDto) {
        return oAuthService.getOAuthUserInfo(loginDto.authorizationCode())
                .flatMap(userInfo -> userRepository.findById(userInfo.id())
                        .switchIfEmpty(userRepository.save(User.create(userInfo.id(), userInfo.name()))));
    }
}
