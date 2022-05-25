package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.annotation.Nullable;
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
public class ServerboundKeyPacket implements Packet {
    private final @NonNull byte[] sharedKey;
    private final @Nullable byte[] verifyToken;
    private final @Nullable Long salt;
    private final @Nullable byte[] signature;

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, byte[] verifyToken) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.verifyToken = runEncryption(Cipher.ENCRYPT_MODE, publicKey, verifyToken);
        this.salt = null;
        this.signature = null;
    }

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, long salt, byte[] signature) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.salt = salt;
        this.signature = signature;
        this.verifyToken = null;
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return new SecretKeySpec(runEncryption(Cipher.DECRYPT_MODE, privateKey, this.sharedKey), "AES");
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return runEncryption(Cipher.DECRYPT_MODE, privateKey, this.verifyToken);
    }

    public ServerboundKeyPacket(NetInput in) throws IOException {
        this.sharedKey = in.readBytes(in.readVarInt());
        if (in.readBoolean()) {
            this.verifyToken = in.readBytes(in.readVarInt());
            this.salt = null;
            this.signature = null;
        } else {
            this.salt = in.readLong();
            this.signature = in.readBytes(in.readVarInt());
            this.verifyToken = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.sharedKey.length);
        out.writeBytes(this.sharedKey);
        out.writeBoolean(this.verifyToken != null);
        if (this.verifyToken != null) {
            out.writeVarInt(this.verifyToken.length);
            out.writeBytes(this.verifyToken);
        } else {
            out.writeLong(this.salt);
            out.writeVarInt(this.signature.length);
            out.writeBytes(this.signature);
        }
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
