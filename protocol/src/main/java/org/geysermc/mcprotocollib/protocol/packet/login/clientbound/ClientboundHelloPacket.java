package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

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

    public ClientboundHelloPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.serverId = helper.readString(in);
        byte[] publicKey = helper.readByteArray(in);
        this.challenge = helper.readByteArray(in);
        this.shouldAuthenticate = in.readBoolean();

        try {
            this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decode public key.", e);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.serverId);
        byte[] encoded = this.publicKey.getEncoded();
        helper.writeByteArray(out, encoded);
        helper.writeByteArray(out, this.challenge);
        out.writeBoolean(this.shouldAuthenticate);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
