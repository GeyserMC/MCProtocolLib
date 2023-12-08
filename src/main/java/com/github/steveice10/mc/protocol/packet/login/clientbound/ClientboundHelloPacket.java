package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Data
@With
@AllArgsConstructor
public class ClientboundHelloPacket implements MinecraftPacket {
    private final @NotNull String serverId;
    private final @NotNull PublicKey publicKey;
    private final byte @NotNull [] challenge;

    public ClientboundHelloPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.serverId = helper.readString(in);
        byte[] publicKey = helper.readByteArray(in);
        this.challenge = helper.readByteArray(in);

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
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
