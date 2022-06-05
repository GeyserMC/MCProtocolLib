package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundChatPacket implements MinecraftPacket {
    private final @NonNull String message;
    private final long timeStamp;
    private final long salt;
    private final byte[] signature;
    private final boolean signedPreview;

    public ServerboundChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.message = helper.readString(in);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();
        this.signature = helper.readByteArray(in);
        this.signedPreview = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.message);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);
        helper.writeVarInt(out, this.signature.length);
        out.writeBytes(this.signature);
        out.writeBoolean(this.signedPreview);
    }
}
