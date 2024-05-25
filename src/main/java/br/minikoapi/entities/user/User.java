package br.minikoapi.entities.user;


import br.minikoapi.dtos.user.UserRegisterDTO;
import br.minikoapi.entities.validator.IValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails, IValidator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(min = 3, message = "Account name must have between 3 to 32 characters")
    @NotNull(message = "Account name can't be null")
    @Column(unique = true)
    private String name;

    @Email
    @NotNull(message = "Account email can't be null")
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 100, message = "Account password must have at least 8 characters")
    @NotNull(message = "Account password can't be null")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime createdTimestamp;

    public User(UserRegisterDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.role = UserRole.USER;
        this.createdTimestamp = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
