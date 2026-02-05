package org.charitable.app.application.service.user;

import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.port.inbound.user.UserUseCase;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.domain.port.outbound.db.user.UserRepository;

@Singleton
class UserService implements UserUseCase {

    private final UserRepository userRepository;
    public UserService(
            UserRepository repository
    ) {
        this.userRepository = repository;
    }

    @Override
    public User register(UserRegisterRequestDTO user) {
        var newUser = new User(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getLatitude(),
                user.getLongitude(),
                null
        );

        return userRepository.save(newUser);
    }
}
