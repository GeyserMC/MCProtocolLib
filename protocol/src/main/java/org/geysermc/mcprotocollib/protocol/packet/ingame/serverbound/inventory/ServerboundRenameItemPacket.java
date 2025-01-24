package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundRenameItemPacket implements MinecraftPacket {
    private final @NonNull String name;

    public ServerboundRenameItemPacket(ByteBuf in) {
        this.name = MinecraftTypes.readString(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.name);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
