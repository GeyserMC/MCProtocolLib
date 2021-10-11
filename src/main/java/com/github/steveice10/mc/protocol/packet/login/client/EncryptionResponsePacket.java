package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptionResponsePacket implements Packet {
    private @NonNull byte[] sharedKey;
    private @NonNull byte[] verifyToken;

    public EncryptionResponsePacket(PublicKey publicKey, SecretKey secretKey, byte[] verifyToken) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.verifyToken = runEncryption(Cipher.ENCRYPT_MODE, publicKey, verifyToken);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return new SecretKeySpec(runEncryption(Cipher.DECRYPT_MODE, privateKey, this.sharedKey), "AES");
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return runEncryption(Cipher.DECRYPT_MODE, privateKey, this.verifyToken);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.sharedKey = in.readBytes(in.readVarInt());
        this.verifyToken = in.readBytes(in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.sharedKey.length);
        out.writeBytes(this.sharedKey);
        out.writeVarInt(this.verifyToken.length);
        out.writeBytes(this.verifyToken);
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
