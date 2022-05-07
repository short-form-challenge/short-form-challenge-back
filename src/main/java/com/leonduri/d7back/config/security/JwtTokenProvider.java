package com.leonduri.d7back.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component  // @ComponentScan을 통해 스캔 되도록 하여 해당 클래스를 bean으로 관리
public class JwtTokenProvider {

    @Value("spring.jwt.secret")
    // @Value: properties 파일에 setting한 내용을 spirng 변수에 주입
    private String secretKey;

    private final long tokenValidMs = 1000L * 60 * 60; // 한시간
    private final long refreshTokenValidMs = 1000L * 60 * 60 * 24 * 14; // 2주

    private final UserDetailsService userDetailsService;
    // UserDetails: Spring security에서 사용자의 정보를 담는 인터페이스
    // UserDetailsService: Spring security에서 사용자의 정보를 가져오는 인터페이스

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        // claim: a piece of information asserted about a subject
        // name, value 싸으로 구성되며, 특정 정보에 대해 특정 주체가 발급헀음을 확인하는 정보
        // 서명 또는 암호화하여 사용됨.
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMs))
                // .signWith(SignatureAlgorithm.ES256, secretKey)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        // claim: a piece of information asserted about a subject
        // name, value 싸으로 구성되며, 특정 정보에 대해 특정 주체가 발급헀음을 확인하는 정보
        // 서명 또는 암호화하여 사용됨.
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMs))
                // .signWith(SignatureAlgorithm.ES256, secretKey)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // get user pk from jwt
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
        // jwt: JSON Web Token. 인터페이스
        // jws: JSON Web Signature. 디지털 서명 방식의 jwt 구현체. 보통 더 많이 씀
        //      claim 내용이 노출되어 보안성은 낮지만, client가 claim의 데이터 사용 가능.
        // jwe: JSON Web Encryption. 암호화 방식의 jwt 구현체
        //      claim 자체가 암호화되어 보안성은 높지만, client는 claim 내용을 알 수 없음.
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }

    // get JWT from the header of a request
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
