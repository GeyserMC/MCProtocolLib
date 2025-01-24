package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetExperiencePacket implements MinecraftPacket {
    private final float experience;
    private final int level;
    private final int totalExperience;

    public ClientboundSetExperiencePacket(ByteBuf in) {
        this.experience = in.readFloat();
        this.level = MinecraftTypes.readVarInt(in);
        this.totalExperience = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeFloat(this.experience);
        MinecraftTypes.writeVarInt(out, this.level);
        MinecraftTypes.writeVarInt(out, this.totalExperience);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
