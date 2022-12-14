package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import org.cloudburstmc.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundBlockEntityTagQuery implements MinecraftPacket {
    private final int transactionId;
    private final @NonNull Vector3i position;

    public ServerboundBlockEntityTagQuery(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.transactionId = helper.readVarInt(in);
        this.position = helper.readPosition(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.transactionId);
        helper.writePosition(out, this.position);
    }
}
