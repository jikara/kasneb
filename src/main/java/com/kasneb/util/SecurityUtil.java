/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;
import org.hashids.Hashids;

/**
 *
 * @author jikara
 */
public class SecurityUtil {

    private static final String SECRET_KEY = "5662256";

    public static String createJWT(Integer userId, String issuer, String subject, long ttlMillis) {
        ttlMillis =86400000;
        //The JWT signature algorithm we will be using to sign the token
        String id = String.valueOf(userId);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    //Sample method to validate and read the JWT
    public static Integer parseJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return Integer.parseInt(claims.getId());
    }

    public static String createSmsVerificationToken(Integer userId) {
        Hashids hashids = new Hashids(SECRET_KEY);
        return hashids.encode(userId);
    }

    public static String createSmsResetToken() {
        Hashids hashids = new Hashids(SECRET_KEY);
        return hashids.encode(Integer.parseInt(GeneratorUtil.generateRandomId()));
    }

    public static Integer parseSmsToken(String smsToken) {
        Hashids hashids = new Hashids(SECRET_KEY);
        long[] numbers = hashids.decode(smsToken);
        Long userId = numbers[0];
        return userId.intValue();
    }

}
