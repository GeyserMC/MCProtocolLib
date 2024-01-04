package org.geysermc.mc.protocol.packet.ingame.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundCooldownPacket implements MinecraftPacket {
    private final int itemId;
    private final int cooldownTicks;

    public ClientboundCooldownPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.itemId = helper.readVarInt(in);
        this.cooldownTicks = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.itemId);
        helper.writeVarInt(out, this.cooldownTicks);
    }
}
