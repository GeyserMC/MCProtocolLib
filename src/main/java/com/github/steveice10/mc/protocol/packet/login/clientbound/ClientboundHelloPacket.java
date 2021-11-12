package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Data
@With
@AllArgsConstructor
public class ClientboundHelloPacket implements Packet {
    private final @NonNull String serverId;
    private final @NonNull PublicKey publicKey;
    private final @NonNull byte[] verifyToken;

    public ClientboundHelloPacket(NetInput in) throws IOException {
        this.serverId = in.readString();
        byte[] publicKey = in.readBytes(in.readVarInt());
        this.verifyToken = in.readBytes(in.readVarInt());

        try {
            this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (GeneralSecurityException e) {
            throw new IOException("Could not decode public key.", e);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.serverId);
        byte[] encoded = this.publicKey.getEncoded();
        out.writeVarInt(encoded.length);
        out.writeBytes(encoded);
        out.writeVarInt(this.verifyToken.length);
        out.writeBytes(this.verifyToken);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
