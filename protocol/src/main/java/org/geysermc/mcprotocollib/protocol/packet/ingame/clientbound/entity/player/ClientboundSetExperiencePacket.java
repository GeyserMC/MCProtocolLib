package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetExperiencePacket implements MinecraftPacket {
    private final float experience;
    private final int level;
    private final int totalExperience;

    public ClientboundSetExperiencePacket(MinecraftByteBuf buf) {
        this.experience = buf.readFloat();
        this.level = buf.readVarInt();
        this.totalExperience = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(this.experience);
        buf.writeVarInt(this.level);
        buf.writeVarInt(this.totalExperience);
    }
}
