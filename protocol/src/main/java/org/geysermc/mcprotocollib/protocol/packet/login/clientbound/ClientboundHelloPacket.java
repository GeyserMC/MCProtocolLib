package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Data
@With
@AllArgsConstructor
public class ClientboundHelloPacket implements MinecraftPacket {
    private final @NonNull String serverId;
    private final @NonNull PublicKey publicKey;
    private final byte @NonNull [] challenge;
    private final boolean shouldAuthenticate;

    public ClientboundHelloPacket(MinecraftByteBuf buf) {
        this.serverId = buf.readString();
        byte[] publicKey = buf.readByteArray();
        this.challenge = buf.readByteArray();
        this.shouldAuthenticate = buf.readBoolean();

        try {
            this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decode public key.", e);
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.serverId);
        byte[] encoded = this.publicKey.getEncoded();
        buf.writeByteArray(encoded);
        buf.writeByteArray(this.challenge);
        buf.writeBoolean(this.shouldAuthenticate);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
