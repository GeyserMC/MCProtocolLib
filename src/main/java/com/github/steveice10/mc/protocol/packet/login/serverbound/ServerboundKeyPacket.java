package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

@ToString
@EqualsAndHashCode
public class ServerboundKeyPacket implements MinecraftPacket {
    private final @NonNull byte[] sharedKey;
    private final @Nullable byte[] verifyToken;
    private final @Nullable SaltSignaturePair saltSignature;

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, byte[] verifyToken) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.verifyToken = runEncryption(Cipher.ENCRYPT_MODE, publicKey, verifyToken);
        this.saltSignature = null;
    }

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, @NotNull SaltSignaturePair saltSignature) {
        this.sharedKey = runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.saltSignature = saltSignature;
        this.verifyToken = null;
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return new SecretKeySpec(runEncryption(Cipher.DECRYPT_MODE, privateKey, this.sharedKey), "AES");
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return runEncryption(Cipher.DECRYPT_MODE, privateKey, this.verifyToken);
    }

    public boolean verifyWithSaltSignature(ServerboundHelloPacket.ProfilePublicKeyData profilePublicKeyData) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(profilePublicKeyData.getPublicKey());

            signature.update(verifyToken);
            signature.update(toByteArray(saltSignature.getSalt()));

            return signature.verify(saltSignature.getSignature());
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private byte[] toByteArray(long value) {
        // Copied from Guava
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xffL);
            value >>= 8;
        }
        return result;
    }

    public ServerboundKeyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.sharedKey = helper.readByteArray(in);
        if (in.readBoolean()) {
            this.verifyToken = helper.readByteArray(in);
            this.saltSignature = null;
        } else {
            this.saltSignature = new SaltSignaturePair(in.readLong(), helper.readByteArray(in));
            this.verifyToken = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeByteArray(out, this.sharedKey);
        out.writeBoolean(this.verifyToken != null);
        if (this.verifyToken != null) {
            helper.writeByteArray(out, this.verifyToken);
        } else {
            out.writeLong(this.saltSignature.salt);
            helper.writeByteArray(out, this.saltSignature.signature);
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

    public static class SaltSignaturePair {
        private final long salt;
        private final byte[] signature;

        public SaltSignaturePair(long salt, byte[] signature) {
            this.salt = salt;
            this.signature = signature;
        }

        public long getSalt() {
            return salt;
        }

        public byte[] getSignature() {
            return signature;
        }
    }
}
