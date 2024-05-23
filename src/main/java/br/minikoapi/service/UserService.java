package br.minikoapi.service;

import br.minikoapi.dtos.UserDTO;
import br.minikoapi.entities.user.User;
import br.minikoapi.entities.validator.ValidationDTO;
import br.minikoapi.infra.ObjectValidator;
import br.minikoapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private ObjectValidator validator = new ObjectValidator();

    @Autowired
    private UserRepository userRepository;

    public User findUserById(String id) throws Exception {
        return userRepository.findUsersById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public User createUser(UserDTO data) throws Exception {
        User newUser = new User(data);
        ValidationDTO valid = validator.validate(newUser);

        if(valid.error()) throw new Exception("Invalid fields");

        userRepository.save(newUser);
        return newUser;
    }
}
