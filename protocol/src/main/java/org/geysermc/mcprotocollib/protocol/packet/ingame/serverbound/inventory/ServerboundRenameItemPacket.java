package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundRenameItemPacket implements MinecraftPacket {
    private final @NonNull String name;

    public ServerboundRenameItemPacket(MinecraftByteBuf buf) {
        this.name = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.name);
    }
}
