package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public ServerboundChatSessionUpdatePacket(ByteBuf in) {
        this.sessionId = MinecraftTypes.readUUID(in);
        this.expiresAt = in.readLong();
        byte[] keyBytes = MinecraftTypes.readByteArray(in);
        this.keySignature = MinecraftTypes.readByteArray(in);

        PublicKey publicKey;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decode public key.", e);
        }

        this.publicKey = publicKey;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, this.sessionId);
        out.writeLong(this.expiresAt);
        MinecraftTypes.writeByteArray(out, this.publicKey.getEncoded());
        MinecraftTypes.writeByteArray(out, this.keySignature);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
