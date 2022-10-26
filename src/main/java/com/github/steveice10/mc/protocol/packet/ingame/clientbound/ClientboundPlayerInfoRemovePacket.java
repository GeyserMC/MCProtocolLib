package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoRemovePacket implements MinecraftPacket {
    private final List<UUID> profileIds;

    public ClientboundPlayerInfoRemovePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.profileIds = new ArrayList<>();
        int numIds = helper.readVarInt(in);
        for (int i = 0; i < numIds; i++) {
            this.profileIds.add(helper.readUUID(in));
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.profileIds.size());
        for (UUID id : this.profileIds) {
            helper.writeUUID(out, id);
        }
    }
}
