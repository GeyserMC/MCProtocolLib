package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NonNull String username;
    private final @Nullable Long expiresAt;
    private final @Nullable PublicKey publicKey;
    private final byte @Nullable[] keySignature;
    private final @Nullable UUID profileId;

    public ServerboundHelloPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.username = helper.readString(in);
        if (in.readBoolean()) {
            this.expiresAt = in.readLong();
            byte[] publicKey = helper.readByteArray(in);
            this.keySignature = helper.readByteArray(in);

            try {
                this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
            } catch (GeneralSecurityException e) {
                throw new IOException("Could not decode public key.", e);
            }
        } else {
            this.expiresAt = null;
            this.publicKey = null;
            this.keySignature = null;
        }
        if (in.readBoolean()) {
            this.profileId = helper.readUUID(in);
        } else {
            this.profileId = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.username);
        out.writeBoolean(this.publicKey != null);
        if (this.publicKey != null) {
            out.writeLong(this.expiresAt);
            byte[] encoded = this.publicKey.getEncoded();
            helper.writeByteArray(out, encoded);
            helper.writeByteArray(out, this.keySignature);
        }
        out.writeBoolean(this.profileId != null);
        if (this.profileId != null) {
            helper.writeUUID(out, this.profileId);
        }
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
