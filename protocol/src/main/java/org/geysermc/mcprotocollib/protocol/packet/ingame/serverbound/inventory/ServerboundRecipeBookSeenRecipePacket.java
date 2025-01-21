package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookSeenRecipePacket implements MinecraftPacket {
    private final int recipe;

    public ServerboundRecipeBookSeenRecipePacket(ByteBuf in) {
        this.recipe = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.recipe);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
