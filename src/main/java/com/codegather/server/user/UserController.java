package com.codegather.server.user;

import com.codegather.server.user.dto.DtoOfLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<?>> login(@RequestBody DtoOfLogin loginDto) {
        return userService.login(loginDto)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK));
    }
}
