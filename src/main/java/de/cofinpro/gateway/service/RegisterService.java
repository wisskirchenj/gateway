package de.cofinpro.gateway.service;

import de.cofinpro.gateway.user.User;
import de.cofinpro.gateway.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository repository;

    /**
     * method receives and saves the User entity with data mapped from the UserDto (name and encrypted password),
     * @param user the prepared User entity to save to the database.
     * @throws UserAlreadyExistsException if user already exists.
     */
    public void registerUser(User user) throws UserAlreadyExistsException {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        repository.save(user);
    }
}
