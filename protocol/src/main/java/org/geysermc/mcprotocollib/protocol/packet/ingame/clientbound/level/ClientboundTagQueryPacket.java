package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @Nullable NbtMap nbt;

    public ClientboundTagQueryPacket(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.nbt = buf.readCompoundTag();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeAnyTag(this.nbt);
    }
}
