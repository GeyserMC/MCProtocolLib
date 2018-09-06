package com.github.steveice10.mc.protocol.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class CryptUtil {
    private CryptUtil() {
    }

    public static SecretKey generateSharedKey() {
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);
            return gen.generateKey();
        } catch(NoSuchAlgorithmException e) {
            throw new Error("Failed to generate shared key.", e);
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            return gen.generateKeyPair();
        } catch(NoSuchAlgorithmException e) {
            throw new Error("Failed to generate key pair.", e);
        }
    }

    public static PublicKey decodePublicKey(byte bytes[]) throws IOException {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        } catch(GeneralSecurityException e) {
            throw new IOException("Could not decrypt public key.", e);
        }
    }

    public static SecretKey decryptSharedKey(PrivateKey privateKey, byte[] sharedKey) {
        return new SecretKeySpec(decryptData(privateKey, sharedKey), "AES");
    }

    public static byte[] encryptData(Key key, byte[] data) {
        return runEncryption(Cipher.ENCRYPT_MODE, key, data);
    }

    public static byte[] decryptData(Key key, byte[] data) {
        return runEncryption(Cipher.DECRYPT_MODE, key, data);
    }

    private static byte[] runEncryption(int mode, Key key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch(GeneralSecurityException e) {
            throw new Error("Failed to run encryption.", e);
        }
    }

    public static byte[] getServerIdHash(String serverId, PublicKey publicKey, SecretKey secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(serverId.getBytes("ISO_8859_1"));
            digest.update(secretKey.getEncoded());
            digest.update(publicKey.getEncoded());
            return digest.digest();
        } catch(Exception e) {
            throw new Error("Failed to generate server ID hash.", e);
        }
    }
}
