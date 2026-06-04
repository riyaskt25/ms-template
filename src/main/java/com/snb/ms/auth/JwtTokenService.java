package com.snb.ms.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  public static final String CLAIM_USER_ID = "userId";
  public static final String CLAIM_AUTHORITIES = "authorities";

  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtTokenService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
  }

  public String generateToken(AuthenticatedUserPrincipal principal) {
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plus(jwtProperties.getExpirationMinutes(), ChronoUnit.MINUTES);
    List<String> authorities =
        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    return Jwts.builder()
        .issuer(jwtProperties.getIssuer())
        .subject(String.valueOf(principal.getUserId()))
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiresAt))
        .claim(CLAIM_USER_ID, principal.getUserId())
        .claim("username", principal.getUsername())
        .claim(CLAIM_AUTHORITIES, authorities)
        .signWith(secretKey)
        .compact();
  }

  public boolean isTokenValid(String token) {
    parseClaims(token);
    return true;
  }

  public AuthenticatedUserPrincipal toPrincipal(String token) {
    Claims claims = parseClaims(token).getPayload();
    Long userId = toLong(claims.get(CLAIM_USER_ID));
    String username = claims.get("username", String.class);
    List<String> authorities = claims.get(CLAIM_AUTHORITIES, List.class);
    Set<org.springframework.security.core.authority.SimpleGrantedAuthority> grantedAuthorities =
        new LinkedHashSet<>();
    if (authorities != null) {
      grantedAuthorities.addAll(
          authorities.stream()
              .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
              .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    return AuthenticatedUserPrincipal.builder()
        .userId(userId)
        .username(username)
        .password("")
        .enabled(true)
        .accountNonLocked(true)
        .authorities(grantedAuthorities)
        .build();
  }

  public Instant extractExpiresAt(String token) {
    return parseClaims(token).getPayload().getExpiration().toInstant();
  }

  private Jws<Claims> parseClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
  }

  private Long toLong(Object value) {
    if (value instanceof Number number) {
      return number.longValue();
    }
    if (value == null) {
      return null;
    }
    return Long.parseLong(String.valueOf(value));
  }
}
