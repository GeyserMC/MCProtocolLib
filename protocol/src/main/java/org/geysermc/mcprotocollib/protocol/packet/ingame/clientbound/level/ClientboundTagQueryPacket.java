package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @Nullable NbtMap nbt;

    public ClientboundTagQueryPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.transactionId = helper.readVarInt(in);
        this.nbt = helper.readCompoundTag(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.transactionId);
        helper.writeAnyTag(out, this.nbt);
    }
}
