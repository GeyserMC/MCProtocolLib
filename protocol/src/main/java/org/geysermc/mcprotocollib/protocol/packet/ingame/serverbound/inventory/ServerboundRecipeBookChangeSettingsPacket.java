package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.CraftingBookStateType;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookChangeSettingsPacket implements MinecraftPacket {
    private final @NonNull CraftingBookStateType type;
    private final boolean bookOpen;
    private final boolean filterActive;

    public ServerboundRecipeBookChangeSettingsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.type = CraftingBookStateType.from(helper.readVarInt(in));
        this.bookOpen = in.readBoolean();
        this.filterActive = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.type.ordinal());
        out.writeBoolean(this.bookOpen);
        out.writeBoolean(this.filterActive);
    }
}
