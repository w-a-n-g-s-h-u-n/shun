package org.wangshun.shun.sample.jwt.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class JwtHelper {

    private static final JWSHeader header;
    private static final RSAKey rootKey;
    private static final RSAPrivateKey privateKey;
    private static final RSAPublicKey publicKey;
    static {
        try {
            header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
            rootKey = new RSAKeyGenerator(2048).generate();
            privateKey = rootKey.toRSAPrivateKey();
            publicKey = rootKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new RuntimeException("生成密钥失败");
        }
    }

    public static String sign(String subject) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(header, new JWTClaimsSet.Builder()//
            .subject(subject)//
            .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))//
            .jwtID(IdWorker.getIdStr())//
            .build());
        signedJWT.sign(new RSASSASigner(privateKey));
        return signedJWT.serialize();
    }

    public static RSAPublicKey getPublickey() {
        return publicKey;
    }
}
