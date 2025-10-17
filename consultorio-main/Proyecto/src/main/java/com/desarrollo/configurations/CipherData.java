package com.desarrollo.configurations;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ivanLuna
 */
@Component
public class CipherData {

    private SignatureAlgorithm signatureAlgorithm;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public SignatureAlgorithm getSignatureAlgorithm() { return signatureAlgorithm; }
    public PrivateKey getPrivateKey() { return privateKey; }
    public PublicKey getPublicKey() { return publicKey; }

    private static final String PASSPHRASE = "proyecto";
    // Una sal fija para derivación determinista (idealmente cámbiala y mantenla externa/configurable)
    private static final byte[] SALT = "configuracion".getBytes(StandardCharsets.UTF_8);

    public CipherData() {
        try {
            // Deriva una semilla desde la palabra "proyecto"
            byte[] seed = deriveSeedFromPassphrase(PASSPHRASE, SALT, 100_000, 32);

            // Genera un par de llaves RSA de manera determinista con esa semilla
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, new SecureRandom(seed));
            KeyPair keyPair = kpg.generateKeyPair();

            // Reconstruye privateKey usando PKCS8EncodedKeySpec + KeyFactory
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            this.privateKey = kf.generatePrivate(pkcs8);

            // Reconstruye publicKey usando X509EncodedKeySpec + KeyFactory
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            this.publicKey = kf.generatePublic(x509);

            this.signatureAlgorithm = SignatureAlgorithm.RS512;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar claves y algoritmo de firma", e);
        }
    }

    private static byte[] deriveSeedFromPassphrase(String passphrase, byte[] salt, int iterations, int lengthBytes) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, lengthBytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

}
