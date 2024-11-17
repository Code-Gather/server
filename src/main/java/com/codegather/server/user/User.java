package com.codegather.server.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document
public class User {
    @Id
    private String id;

    private String username;

    public static User create(String oAuthId, String username) {
        return User.builder()
                .id(oAuthId)
                .username(username)
                .build();
    }
}
