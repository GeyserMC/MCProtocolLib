package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundChatPacket implements Packet {
    private final @NonNull String message;
    private final long timeStamp;
    private final long salt;
    private final byte[] signature;
    private final boolean signedPreview;

    public ServerboundChatPacket(NetInput in) throws IOException {
        this.message = in.readString();
        this.timeStamp = in.readLong();
        this.salt = in.readLong();
        this.signature = in.readBytes(in.readVarInt());
        this.signedPreview = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.message);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);
        out.writeVarInt(this.signature.length);
        out.writeBytes(this.signature);
        out.writeBoolean(this.signedPreview);
    }
}
