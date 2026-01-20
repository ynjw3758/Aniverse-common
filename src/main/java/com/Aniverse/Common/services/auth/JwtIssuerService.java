package com.Aniverse.Common.services.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtIssuerService {
	/*
	  private final PrivateKey privateKey;
	  private final String kid;
	  private final String issuer;
	  private final String audience;
	  
	  public JwtIssuerService(PrivateKey privateKey,
              @Value("${jwt.kid}") String kid,
              @Value("${jwt.issuer}") String issuer,
              @Value("${jwt.audience}") String audience) {
					this.privateKey = privateKey;
					this.kid = kid;
					this.issuer = issuer;
					this.audience = audience;
					}
	  
	  public String issueAccessToken(String userId, List<String> roles, Duration ttl) {
		    try {
		      Instant now = Instant.now();
		      JWTClaimsSet claims = new JWTClaimsSet.Builder()
		          .issuer(issuer)
		          .subject(userId)
		          .audience(audience)
		          .issueTime(Date.from(now))
		          .expirationTime(Date.from(now.plus(ttl)))          // 액세스 토큰 만료
		          .claim("roles", roles)
		          .build();

		      JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
		          .type(JOSEObjectType.JWT).keyID(kid).build();

		      SignedJWT jwt = new SignedJWT(header, claims);
		      jwt.sign(new RSASSASigner((RSAPrivateKey) privateKey));
		      return jwt.serialize();
		    } catch (Exception e) {
		      throw new RuntimeException("Issue token failed", e);
		    }
		  }
*/
}
