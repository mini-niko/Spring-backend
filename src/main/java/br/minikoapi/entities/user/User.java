package br.minikoapi.entities.user;


import br.minikoapi.dtos.UserDTO;
import br.minikoapi.entities.validator.IValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements IValidator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(min = 3, max = 32, message = "Account name must have between 3 to 32 characters")
    @NotNull(message = "Account name can't be null")
    @Column(unique = true)
    private String name;

    @Email
    @NotNull(message = "Account email can't be null")
    @Column(unique = true)
    private String email;

    @Size(min = 3, max = 32, message = "Account password must have between 3 to 32 characters")
    @NotNull(message = "Account password can't be null")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private LocalDateTime createdTimestamp;

    public User(UserDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.userRole = UserRole.ADMIN;
        this.createdTimestamp = LocalDateTime.now();
    }
}
