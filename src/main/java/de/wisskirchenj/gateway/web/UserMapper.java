package de.wisskirchenj.gateway.web;

import de.wisskirchenj.gateway.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * mapper to map received UserDto on register to a User entity, hereby encoding the raw password.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * map the Dto to the entity and encode the password hereby.
     */
    public User toEntity(UserDto dto) {
        return new User().setUsername(dto.email())
                .setPassword(passwordEncoder.encode(dto.password()));
    }
}
