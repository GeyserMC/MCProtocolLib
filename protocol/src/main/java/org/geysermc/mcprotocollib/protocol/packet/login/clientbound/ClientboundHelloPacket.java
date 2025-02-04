package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public ClientboundHelloPacket(ByteBuf in) {
        this.serverId = MinecraftTypes.readString(in);
        byte[] publicKey = MinecraftTypes.readByteArray(in);
        this.challenge = MinecraftTypes.readByteArray(in);
        this.shouldAuthenticate = in.readBoolean();

        try {
            this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decode public key.", e);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.serverId);
        byte[] encoded = this.publicKey.getEncoded();
        MinecraftTypes.writeByteArray(out, encoded);
        MinecraftTypes.writeByteArray(out, this.challenge);
        out.writeBoolean(this.shouldAuthenticate);
    }
}
