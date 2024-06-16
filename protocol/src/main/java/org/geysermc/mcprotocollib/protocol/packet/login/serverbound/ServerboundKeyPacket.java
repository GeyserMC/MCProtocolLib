package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

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
    private final byte @NonNull [] sharedKey;
    private final byte @NonNull [] encryptedChallenge;

    public ServerboundKeyPacket(PublicKey publicKey, SecretKey secretKey, byte[] encryptedChallenge) {
        this.sharedKey = cipherData(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
        this.encryptedChallenge = cipherData(Cipher.ENCRYPT_MODE, publicKey, encryptedChallenge);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return new SecretKeySpec(cipherData(Cipher.DECRYPT_MODE, privateKey, this.sharedKey), "AES");
    }

    public byte[] getDecryptedChallenge(PrivateKey privateKey) {
        return cipherData(Cipher.DECRYPT_MODE, privateKey, this.encryptedChallenge);
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

    private static byte[] cipherData(int mode, Key key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to cipher data.", e);
        }
    }
}
