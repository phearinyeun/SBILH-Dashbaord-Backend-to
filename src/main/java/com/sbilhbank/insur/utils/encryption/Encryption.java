package com.sbilhbank.insur.utils.encryption;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Encryption {
    public static String doRSASignature(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }
    public static boolean doRSAVerify(String plainText, String signature, PublicKey publicKey) throws Exception{
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }
    public static PrivateKey getPrivateKeyBySignatureText(String privateSignature) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        byte[] content = privateSignature.getBytes(StandardCharsets.UTF_8);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
        return  factory.generatePrivate(privateKeySpec);
    }
    public static PrivateKey getPrivateKey() throws Exception{
        InputStream is  = Encryption.class.getResourceAsStream("/keys/privatekey_mob.pem");
        InputStreamReader isRead = new InputStreamReader(is);
        PemReader pemReader = new PemReader(isRead);
        byte[] keyBytes = new byte[is.available()];
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
        return  factory.generatePrivate(privateKeySpec);
    }
    public static PublicKey getPublicKeyBySignatureText (String publicSignature) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        byte[] content = publicSignature.getBytes(StandardCharsets.UTF_8);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }
    public static PublicKey getPublicKey() throws Exception {
        InputStream is  = Encryption.class.getResourceAsStream("/keys/publickey_mob.pem");
        InputStreamReader isRead = new InputStreamReader(is);
        PemReader pemReader = new PemReader(isRead);
        byte[] keyBytes = new byte[is.available()];
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }
}
