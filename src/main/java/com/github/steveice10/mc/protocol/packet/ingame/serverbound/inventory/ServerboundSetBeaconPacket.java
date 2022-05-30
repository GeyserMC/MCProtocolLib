package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;
import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements MinecraftPacket {
    private final OptionalInt primaryEffect;
    private final OptionalInt secondaryEffect;

    public ServerboundSetBeaconPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        if (in.readBoolean()) {
            this.primaryEffect = OptionalInt.of(helper.readVarInt(in));
        } else {
            this.primaryEffect = OptionalInt.empty();
        }

        if (in.readBoolean()) {
            this.secondaryEffect = OptionalInt.of(helper.readVarInt(in));
        } else {
            this.secondaryEffect = OptionalInt.empty();
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeBoolean(this.primaryEffect.isPresent());
        if (this.primaryEffect.isPresent()) {
            helper.writeVarInt(out, this.primaryEffect.getAsInt());
        }

        out.writeBoolean(this.secondaryEffect.isPresent());
        if (this.secondaryEffect.isPresent()) {
            helper.writeVarInt(out, this.secondaryEffect.getAsInt());
        }
    }
}
