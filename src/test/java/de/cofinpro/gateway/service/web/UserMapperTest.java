package de.cofinpro.gateway.service.web;

import de.cofinpro.gateway.user.User;
import de.cofinpro.gateway.web.UserDto;
import de.cofinpro.gateway.web.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {
    UserDto dto;
    UserMapper mapper;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        mapper = new UserMapper(passwordEncoder);
        dto = new UserDto("a@b.c", "secret");
    }

    @Test
    void toEntity() {
        User mapped = mapper.toEntity(dto);
        assertEquals(dto.email(), mapped.getUsername());
        assertTrue(passwordEncoder.matches(dto.password(), mapped.getPassword()));
    }
}