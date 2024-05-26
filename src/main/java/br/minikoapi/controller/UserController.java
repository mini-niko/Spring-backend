package br.minikoapi.controller;

import br.minikoapi.dtos.user.UserLoginDTO;
import br.minikoapi.dtos.user.UserRegisterDTO;
import br.minikoapi.entities.user.User;
import br.minikoapi.infra.security.TokenService;
import br.minikoapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRegisterDTO data) throws Exception {
        User newUser = userService.createUser(data);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDTO data) {
        userService.findUserByEmail(data.email());

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/find-user")
    public ResponseEntity<User> findUser(@RequestParam String id) throws Exception {
        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }
}
