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
public class ServerboundPlaceRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final int recipe;
    private final boolean useMaxItems;

    public ServerboundPlaceRecipePacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.recipe = MinecraftTypes.readVarInt(in);
        this.useMaxItems = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.recipe);
        out.writeBoolean(this.useMaxItems);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
