package com.minerva.minervaapi.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.minerva.minervaapi.controllers.dtos.TokenResponseDTO;
import com.minerva.minervaapi.exceptions.ServerErrorException;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.services.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${auth.jwt.token.secret}")
    private String secretToken;

    @Value("${session.expiration.time}")
    private String durationToken;

    private final String issuer = "minerva";

    public TokenResponseDTO generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretToken);
            Instant expires = this.generateExpirationDate();

            String token = JWT.create()
                    .withIssuer(this.issuer)
                    .withSubject(user.getId().toString())
                    .withExpiresAt(expires)
                    .sign(algorithm);

            return new TokenResponseDTO(token, expires);
        } catch (JWTCreationException exception) {
            throw new ServerErrorException("Algo deu errado, tente novamente mais tarde");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretToken);

            return JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        int jwtDuration = Integer.parseInt(this.durationToken);
        return LocalDateTime.now().plusHours(jwtDuration).toInstant(ZoneOffset.UTC);
    }
}
