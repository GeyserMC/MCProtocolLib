package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

@ToString
@EqualsAndHashCode
public class ServerboundKeyPacket implements MinecraftPacket {
    private final byte @NonNull[] sharedKey;
    private final byte @NonNull[] encryptedChallenge;

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, byte[] challenge) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.encryptedChallenge = runEncryption(Cipher.ENCRYPT_MODE, publicKey, challenge);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return new SecretKeySpec(runEncryption(Cipher.DECRYPT_MODE, privateKey, this.sharedKey), "AES");
    }

    public byte[] getEncryptedChallenge(PrivateKey privateKey) {
        return runEncryption(Cipher.DECRYPT_MODE, privateKey, this.encryptedChallenge);
    }

    public ServerboundKeyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.sharedKey = helper.readByteArray(in);
        this.encryptedChallenge = helper.readByteArray(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeByteArray(out, this.sharedKey);
        helper.writeByteArray(out, this.encryptedChallenge);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    private static byte[] runEncryption(int mode, Key key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm().equals("RSA") ? "RSA/ECB/PKCS1Padding" : "AES/CFB8/NoPadding");
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to " + (mode == Cipher.DECRYPT_MODE ? "decrypt" : "encrypt") + " data.", e);
        }
    }
}
