package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundCooldownPacket implements MinecraftPacket {
    private final Key cooldownGroup;
    private final int cooldownTicks;

    public ClientboundCooldownPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.cooldownGroup = helper.readResourceLocation(in);
        this.cooldownTicks = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.cooldownGroup);
        helper.writeVarInt(out, this.cooldownTicks);
    }
}
