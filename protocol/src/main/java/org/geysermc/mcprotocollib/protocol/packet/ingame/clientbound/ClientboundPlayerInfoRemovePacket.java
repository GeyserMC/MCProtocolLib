package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoRemovePacket implements MinecraftPacket {
    private final List<UUID> profileIds;

    public ClientboundPlayerInfoRemovePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.profileIds = helper.readList(in, helper::readUUID);
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeList(out, this.profileIds, helper::writeUUID);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
