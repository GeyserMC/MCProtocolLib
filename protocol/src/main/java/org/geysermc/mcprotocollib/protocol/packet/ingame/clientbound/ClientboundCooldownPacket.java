package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundCooldownPacket implements MinecraftPacket {
    private final Key cooldownGroup;
    private final int cooldownTicks;

    public ClientboundCooldownPacket(ByteBuf in) {
        this.cooldownGroup = MinecraftTypes.readResourceLocation(in);
        this.cooldownTicks = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.cooldownGroup);
        MinecraftTypes.writeVarInt(out, this.cooldownTicks);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
