package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public ServerboundKeyPacket(ByteBuf in) {
        this.sharedKey = MinecraftTypes.readByteArray(in);
        this.encryptedChallenge = MinecraftTypes.readByteArray(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeByteArray(out, this.sharedKey);
        MinecraftTypes.writeByteArray(out, this.encryptedChallenge);
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
