package br.minikoapi.infra.security;

import br.minikoapi.entities.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Algorithm algorithm;

    public String generateToken(User user) {
        try {
            if(algorithm == null)
                algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getId())
                .sign(algorithm);
            return token;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            if(algorithm == null)
                algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return "";
        }
    }
}
