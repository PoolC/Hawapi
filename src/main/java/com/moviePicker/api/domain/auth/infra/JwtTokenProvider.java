package com.moviePicker.api.domain.auth.infra;

import com.moviePicker.api.domain.auth.exception.UnauthenticatedException;
import com.moviePicker.api.domain.member.domain.Member;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final int AUTHORIZATION_CONSTRUCTION_LENGTH = 2;

    //TODO: 환경 변수로 변경해야함
    private String secretKey = "EXAMPLE_SECRET_KEY";

    //TODO: 환경 변수로 변경해야함
    private Long expireTimeInMS = 360000L;

    public String createToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setIssuer("MoviePicker/Hawapi")
                .setSubject(member.getUUID())
                .setExpiration(new Date(now.getTime() + expireTimeInMS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (isEmptyToken(token)) {
            return null;
        }

        return getPayloadIfBearerToken(token);
    }

    public String getSubject(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthenticatedException("해당 토큰은 잘못되었습니다. " + e.getMessage());
        }
    }

    private boolean isEmptyToken(String token) {
        return !StringUtils.hasText(token) || token.equals("Bearer");
    }

    private String getPayloadIfBearerToken(String token) {
        String[] typeAndCredentials = token.split(" ");

        checkIsBearerToken(typeAndCredentials);

        return typeAndCredentials[1];
    }

    private void checkIsBearerToken(String[] typeAndCredentials) {
        checkIsCorrectToken(typeAndCredentials.length);
        checkIsSupportedAuthorizationType(typeAndCredentials[0]);
    }

    private void checkIsCorrectToken(int typeAndCredentialsLength) {
        if (typeAndCredentialsLength != AUTHORIZATION_CONSTRUCTION_LENGTH) {
            throw new IllegalArgumentException("해당 토큰은 잘못되었습니다.");
        }
    }

    private void checkIsSupportedAuthorizationType(String authorizationType) {
        if (!authorizationType.equals("Bearer")) {
            throw new JwtException("Bear 이외의 Authorization scheme는 지원하지 않습니다.");
        }
    }
}
