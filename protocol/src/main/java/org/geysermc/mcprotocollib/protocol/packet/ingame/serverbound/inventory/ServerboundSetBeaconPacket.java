package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements MinecraftPacket {
    private final OptionalInt primaryEffect;
    private final OptionalInt secondaryEffect;

    public ServerboundSetBeaconPacket(ByteBuf in, MinecraftCodecHelper helper) {
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
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
