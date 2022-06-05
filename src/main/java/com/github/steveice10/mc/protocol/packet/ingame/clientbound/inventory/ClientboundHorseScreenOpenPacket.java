package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundHorseScreenOpenPacket implements MinecraftPacket {
    private final int containerId;
    private final int numberOfSlots;
    private final int entityId;

    public ClientboundHorseScreenOpenPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.containerId = in.readByte();
        this.numberOfSlots = helper.readVarInt(in);
        this.entityId = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeByte(this.containerId);
        helper.writeVarInt(out, this.numberOfSlots);
        out.writeInt(this.entityId);
    }
}
