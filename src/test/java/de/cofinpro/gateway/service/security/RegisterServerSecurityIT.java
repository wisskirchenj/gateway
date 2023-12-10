package de.cofinpro.gateway.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.gateway.web.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/userstest",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
@DisabledInAotMode // bug in Spring 3.2 @MockBean does not work in AOT mode (https://github.com/spring-projects/spring-boot/issues/36997)
class RegisterServerSecurityIT {

    // needed since otherwise test tries to connect to Authorization server on AppContext creation
    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void whenLoginUrlUnauthenticated_302RedirectToAuthServer() throws Exception {
        mockMvc.perform(post("/oauth2/token")
                        .content("something"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void whenFalseUrlUnauthenticated_302RedirectToAuthServer() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void registerUnauthenticatedValidJson_AddsUser() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("hans.wurst@xyz.de", "12345678"))))
                .andExpect(status().isOk());
    }

    @Test
    void registerUnauthenticatedExistingUser_Gives400() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("test@xyz.de", "12345678"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/register") // and again
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("test@xyz.de", "12345678"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUnauthenticatedInvalidDto_Gives400() throws Exception {
        mockMvc.perform(post("/api/register") // and again
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("wrong", "1234"))))
                .andExpect(status().isBadRequest());
    }
}
