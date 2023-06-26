package de.cofinpro.register.service;

import de.cofinpro.register.user.User;
import de.cofinpro.register.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class RegisterServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegisterService service;

    @Test
    void whenUserExist_RegisterUserThrows() {
        when(userRepository.existsByUsername("test")).thenReturn(true);
        var newUser = new User().setUsername("test");
        assertThrows(UserAlreadyExistsException.class, () -> service.registerUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenRegisterUser_UserSaved() {
        when(userRepository.existsByUsername("test")).thenReturn(false);
        var newUser = new User().setUsername("test");
        service.registerUser(newUser);
        verify(userRepository).save(newUser);
    }
}