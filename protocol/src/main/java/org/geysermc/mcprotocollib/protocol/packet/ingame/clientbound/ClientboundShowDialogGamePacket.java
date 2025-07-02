package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;

@Data
@With
@AllArgsConstructor
public class ClientboundShowDialogGamePacket implements MinecraftPacket {
    private final Holder<NbtMap> dialog;

    public ClientboundShowDialogGamePacket(ByteBuf in) {
        this.dialog = MinecraftTypes.readHolder(in, MinecraftTypes::readCompoundTag);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeHolder(out, this.dialog, MinecraftTypes::writeAnyTag);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
