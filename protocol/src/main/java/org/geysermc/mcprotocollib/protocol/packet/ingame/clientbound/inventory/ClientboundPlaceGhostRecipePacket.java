package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlaceGhostRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull String recipeId;

    public ClientboundPlaceGhostRecipePacket(MinecraftByteBuf buf) {
        this.containerId = buf.readByte();
        this.recipeId = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeString(this.recipeId);
    }
}
