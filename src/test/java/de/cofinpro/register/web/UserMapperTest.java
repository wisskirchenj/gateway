package de.cofinpro.register.web;

import de.cofinpro.register.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

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