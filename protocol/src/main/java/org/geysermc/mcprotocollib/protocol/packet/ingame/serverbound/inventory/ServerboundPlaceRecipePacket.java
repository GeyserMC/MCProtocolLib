package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPlaceRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull String recipeId;
    private final boolean makeAll;

    public ServerboundPlaceRecipePacket(MinecraftByteBuf buf) {
        this.containerId = buf.readByte();
        this.recipeId = buf.readString();
        this.makeAll = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeString(this.recipeId);
        buf.writeBoolean(this.makeAll);
    }
}
