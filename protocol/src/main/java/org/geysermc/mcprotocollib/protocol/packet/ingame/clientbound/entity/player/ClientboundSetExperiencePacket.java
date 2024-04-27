package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetExperiencePacket implements MinecraftPacket {
    private final float experience;
    private final int level;
    private final int totalExperience;

    public ClientboundSetExperiencePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.experience = in.readFloat();
        this.level = helper.readVarInt(in);
        this.totalExperience = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.experience);
        helper.writeVarInt(out, this.level);
        helper.writeVarInt(out, this.totalExperience);
    }
}
