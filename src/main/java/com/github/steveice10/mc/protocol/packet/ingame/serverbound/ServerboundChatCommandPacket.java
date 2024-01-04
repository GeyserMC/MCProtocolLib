package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundChatCommandPacket implements MinecraftPacket {
    private final String command;
    private final long timeStamp;
    private final long salt;
    private final List<ArgumentSignature> signatures;
    private final int offset;
    private final BitSet acknowledgedMessages;

    public ServerboundChatCommandPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.command = helper.readString(in);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();
        this.signatures = new ArrayList<>();
        int signatureCount = Math.min(helper.readVarInt(in), 8);
        for (int i = 0; i < signatureCount; i++) {
            byte[] signature = new byte[256];
            signatures.add(new ArgumentSignature(helper.readString(in, 16), signature));
            in.readBytes(signature);
        }

        this.offset = helper.readVarInt(in);
        this.acknowledgedMessages = helper.readFixedBitSet(in, 20);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.command);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);
        helper.writeVarInt(out, this.signatures.size());
        for (ArgumentSignature signature : this.signatures) {
            helper.writeString(out, signature.getName());
            out.writeBytes(signature.getSignature());
        }

        helper.writeVarInt(out, this.offset);
        helper.writeFixedBitSet(out, this.acknowledgedMessages, 20);
    }
}
