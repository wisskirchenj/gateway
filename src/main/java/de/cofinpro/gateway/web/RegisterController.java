package de.cofinpro.gateway.web;

import de.cofinpro.gateway.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService service;
    private final UserMapper mapper;

    /**
     * register endpoint - unauthenticated (!).
     * @param userDto dto containing provided user email (=username) and raw password
     * @return empty response 200(OK) on successful register, 400(BadRequest) if dto validation fails or user exists
     */
    @PostMapping
    ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto userDto) {
        service.registerUser(mapper.toEntity(userDto));
        return ok().build();
    }
}
