package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundSetCursorItemPacket implements MinecraftPacket {
    private final @Nullable ItemStack contents;

    public ClientboundSetCursorItemPacket(ByteBuf in) {
        this.contents = MinecraftTypes.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeOptionalItemStack(out, this.contents);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
