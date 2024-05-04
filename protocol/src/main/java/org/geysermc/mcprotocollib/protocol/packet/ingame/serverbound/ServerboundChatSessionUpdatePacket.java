package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundChatSessionUpdatePacket implements MinecraftPacket {
    private final UUID sessionId;
    private final long expiresAt;
    private final PublicKey publicKey;
    private final byte[] keySignature;

    public ServerboundChatSessionUpdatePacket(MinecraftByteBuf buf) {
        this.sessionId = buf.readUUID();
        this.expiresAt = buf.readLong();
        byte[] keyBytes = buf.readByteArray();
        this.keySignature = buf.readByteArray();

        PublicKey publicKey;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decode public key.", e);
        }

        this.publicKey = publicKey;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.sessionId);
        buf.writeLong(this.expiresAt);
        buf.writeByteArray(this.publicKey.getEncoded());
        buf.writeByteArray(this.keySignature);
    }
}
