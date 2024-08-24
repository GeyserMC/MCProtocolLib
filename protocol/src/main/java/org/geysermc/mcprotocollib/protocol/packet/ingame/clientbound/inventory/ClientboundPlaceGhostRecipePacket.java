package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlaceGhostRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull String recipeId;

    public ClientboundPlaceGhostRecipePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);
        this.recipeId = helper.readString(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);
        helper.writeString(out, this.recipeId);
    }
}
