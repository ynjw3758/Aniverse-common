package com.Aniverse.Common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class KeyLoad {
	/*
	  @Value("${jwt.private-key-path}")
	  private String privateKeyPath;

	  @Value("${jwt.public-key-path}")
	  private String publicKeyPath;
	  
	  
	  @Bean
	  public PrivateKey jwtPrivateKey() {
	    try {
	      String pem = Files.readString(Path.of(privateKeyPath));
	      String base64 = pem.replace("-----BEGIN PRIVATE KEY-----","")
	                         .replace("-----END PRIVATE KEY-----","")
	                         .replaceAll("\\s","");
	      byte[] der = Base64.getDecoder().decode(base64);
	      return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
	    } catch (Exception e) {
	      throw new IllegalStateException("Load PrivateKey failed", e);
	    }
	  }

	  @Bean
	  public RSAPublicKey jwtPublicKey() {
	    try {
	      String pem = Files.readString(Path.of(publicKeyPath));
	      String base64 = pem.replace("-----BEGIN PUBLIC KEY-----","")
	                         .replace("-----END PUBLIC KEY-----","")
	                         .replaceAll("\\s","");
	      byte[] der = Base64.getDecoder().decode(base64);
	      return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
	    } catch (Exception e) {
	      throw new IllegalStateException("Load PublicKey failed", e);
	    }
	  }
	  
	  
*/
}
