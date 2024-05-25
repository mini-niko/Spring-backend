package br.minikoapi.service;

import br.minikoapi.dtos.user.UserRegisterDTO;
import br.minikoapi.entities.user.User;
import br.minikoapi.entities.validator.ValidationDTO;
import br.minikoapi.infra.ObjectValidator;
import br.minikoapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private ObjectValidator validator = new ObjectValidator();

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUsersByEmail(username).orElseThrow(EntityNotFoundException::new);
    }

    public User findUserById(String id) {
        return userRepository.findUsersById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUsersByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public User createUser(UserRegisterDTO data) throws Exception {
        User userValidate = new User(data);
        ValidationDTO valid = validator.validate(userValidate);

        if(valid.error()) throw new Exception("Invalid fields");

        User userEncrypted = new User(new UserRegisterDTO(
                data.name(),
                data.email(),
                new BCryptPasswordEncoder().encode(data.password())
        ));

        userRepository.save(userEncrypted);
        return userEncrypted;
    }
}
