package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundTagQueryPacket implements MinecraftPacket {
    private final int transactionId;
    private final @Nullable CompoundTag nbt;

    public ClientboundTagQueryPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.transactionId = helper.readVarInt(in);
        this.nbt = helper.readAnyTag(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.transactionId);
        helper.writeAnyTag(out, this.nbt);
    }
}
