package de.cofinpro.gateway.service.security;

import de.cofinpro.gateway.service.RegisterService;
import de.cofinpro.gateway.web.UserDto;
import de.cofinpro.gateway.web.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:postgresql://localhost:5432/userstest",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureWebTestClient
class RegisterServerSecurityIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    WebTestClient webClient;

    @Autowired
    RegisterService registerService;

    @Autowired
    UserMapper userMapper;

    @Test
    void whenLoginUrlUnauthenticated_302RedirectToAuthServer() throws Exception {
        webClient.post().uri("/oauth2/token")
                .bodyValue("something")
                .exchange()
                .expectStatus().isEqualTo(302);
    }

    @Test
    void whenFalseUrlUnauthenticated_302RedirectToAuthServer() throws Exception {
        webClient.get().uri("/api")
                .exchange()
                .expectStatus().isEqualTo(302);
    }

    @Test
    void registerUnauthenticatedValidJson_AddsUser() throws Exception {
        webClient.post().uri("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserDto("hans.wurst@xyz.de", "12345678"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void registerUnauthenticatedExistingUser_Gives400() throws Exception {
        webClient.post().uri("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserDto("test@xyz.de", "12345678"))
                .exchange()
                .expectStatus().isOk();
        webClient.post().uri("/api/register")       // and again
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserDto("test@xyz.de", "12345678"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void registerUnauthenticatedInvalidDto_Gives400() throws Exception {
        webClient.post().uri("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserDto("wrong", "1234"))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
