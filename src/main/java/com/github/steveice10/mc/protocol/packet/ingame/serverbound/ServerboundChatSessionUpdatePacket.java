package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
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

    public ServerboundChatSessionUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.sessionId = helper.readUUID(in);
        this.expiresAt = in.readLong();
        byte[] keyBytes = helper.readByteArray(in);
        this.keySignature = helper.readByteArray(in);

        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException e) {
            throw new IOException("Could not decode public key.", e);
        }

        this.publicKey = publicKey;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeUUID(out, this.sessionId);
        out.writeLong(this.expiresAt);
        helper.writeByteArray(out, this.publicKey.getEncoded());
        helper.writeByteArray(out, this.keySignature);
    }
}
