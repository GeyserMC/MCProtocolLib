package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.CraftingBookStateType;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookChangeSettingsPacket implements MinecraftPacket {
    private final @NonNull CraftingBookStateType type;
    private final boolean bookOpen;
    private final boolean filterActive;

    public ServerboundRecipeBookChangeSettingsPacket(MinecraftByteBuf buf) {
        this.type = CraftingBookStateType.from(buf.readVarInt());
        this.bookOpen = buf.readBoolean();
        this.filterActive = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.type.ordinal());
        buf.writeBoolean(this.bookOpen);
        buf.writeBoolean(this.filterActive);
    }
}
