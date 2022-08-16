package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NonNull String username;
    private final @Nullable ProfilePublicKeyData publicKey;
    private final @Nullable UUID profileId;

    public ServerboundHelloPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.username = helper.readString(in);
        if (in.readBoolean()) {
            this.publicKey = new ProfilePublicKeyData(in, helper);
        } else {
            this.publicKey = null;
        }
        this.profileId = helper.readNullable(in, helper::readUUID);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.username);
        out.writeBoolean(this.publicKey != null);
        if (this.publicKey != null) {
            this.publicKey.serialize(out, helper);
        }
        helper.writeNullable(out, this.profileId, helper::writeUUID);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    // Likely temporary; will be moved to AuthLib when full public key support is developed
    public static class ProfilePublicKeyData {
        private final long expiresAt;
        private final PublicKey publicKey;
        private final byte[] keySignature;

        public ProfilePublicKeyData(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
            this.expiresAt = in.readLong();
            byte[] publicKey = helper.readByteArray(in);
            this.keySignature = helper.readByteArray(in);

            try {
                this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
            } catch (GeneralSecurityException e) {
                throw new IOException("Could not decode public key.", e);
            }
        }

        public ProfilePublicKeyData(long expiresAt, PublicKey publicKey, byte[] keySignature) {
            this.expiresAt = expiresAt;
            this.publicKey = publicKey;
            this.keySignature = keySignature;
        }

        private void serialize(ByteBuf out, MinecraftCodecHelper helper) {
            out.writeLong(this.expiresAt);
            byte[] encoded = this.publicKey.getEncoded();
            helper.writeByteArray(out, encoded);
            helper.writeByteArray(out, this.keySignature);
        }

        @Contract("-> new")
        public Instant getExpiresAt() {
            return Instant.ofEpochMilli(this.expiresAt);
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public byte[] getKeySignature() {
            return keySignature;
        }
    }
}
