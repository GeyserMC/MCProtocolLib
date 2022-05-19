package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements Packet {
    private final @NonNull String username;
    private final @Nullable Long expiresAt;
    private final @Nullable PublicKey publicKey;
    private final @Nullable byte[] keySignature;

    public ServerboundHelloPacket(NetInput in) throws IOException {
        this.username = in.readString();
        if (in.readBoolean()) {
            this.expiresAt = in.readLong();
            byte[] publicKey = in.readBytes(in.readVarInt());
            this.keySignature = in.readBytes(in.readVarInt());

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
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.username);
        out.writeBoolean(this.publicKey != null);
        if (this.publicKey != null) {
            out.writeLong(this.expiresAt);
            byte[] encoded = this.publicKey.getEncoded();
            out.writeVarInt(encoded.length);
            out.writeBytes(encoded);
            out.writeVarInt(this.keySignature.length);
            out.writeBytes(this.keySignature);
        }
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
