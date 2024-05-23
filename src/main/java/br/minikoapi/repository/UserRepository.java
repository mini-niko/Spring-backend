package br.minikoapi.repository;

import br.minikoapi.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUsersById(String id);
    Optional<User> findUsersByEmail(String email);

}
