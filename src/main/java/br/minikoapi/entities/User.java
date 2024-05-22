package br.minikoapi.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(min = 3, max = 32, message = "Account name must have between 3 to 32 characters")
    @Column(unique = true)
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 3, max = 32, message = "Account password must have between 3 to 32 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Date timestamp;
}
