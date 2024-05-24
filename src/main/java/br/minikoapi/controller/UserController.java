package br.minikoapi.controller;

import br.minikoapi.dtos.UserDTO;
import br.minikoapi.entities.user.User;
import br.minikoapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO data) throws Exception {
        User newUser = userService.createUser(data);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/find-user")
    public ResponseEntity<User> findUser(@RequestParam String id) throws Exception {
        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }
}
