package org.geysermc.mc.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundPickItemPacket implements MinecraftPacket {
    private final int slot;

    public ServerboundPickItemPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.slot);
    }
}
