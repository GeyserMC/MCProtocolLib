package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundShowDialogConfigurationPacket implements MinecraftPacket {
    private final NbtMap dialog;

    public ClientboundShowDialogConfigurationPacket(ByteBuf in) {
        this.dialog = MinecraftTypes.readCompoundTag(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeAnyTag(out, this.dialog);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
