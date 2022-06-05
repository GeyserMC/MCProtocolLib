package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

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
public class ClientboundSetExperiencePacket implements MinecraftPacket {
    private final float experience;
    private final int level;
    private final int totalExperience;

    public ClientboundSetExperiencePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.experience = in.readFloat();
        this.level = helper.readVarInt(in);
        this.totalExperience = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeFloat(this.experience);
        helper.writeVarInt(out, this.level);
        helper.writeVarInt(out, this.totalExperience);
    }
}
