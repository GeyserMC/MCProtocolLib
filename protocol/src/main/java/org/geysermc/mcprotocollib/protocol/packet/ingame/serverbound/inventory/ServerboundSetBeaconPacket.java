package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements MinecraftPacket {
    private final OptionalInt primaryEffect;
    private final OptionalInt secondaryEffect;

    public ServerboundSetBeaconPacket(ByteBuf in) {
        if (in.readBoolean()) {
            this.primaryEffect = OptionalInt.of(MinecraftTypes.readVarInt(in));
        } else {
            this.primaryEffect = OptionalInt.empty();
        }

        if (in.readBoolean()) {
            this.secondaryEffect = OptionalInt.of(MinecraftTypes.readVarInt(in));
        } else {
            this.secondaryEffect = OptionalInt.empty();
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeBoolean(this.primaryEffect.isPresent());
        if (this.primaryEffect.isPresent()) {
            MinecraftTypes.writeVarInt(out, this.primaryEffect.getAsInt());
        }

        out.writeBoolean(this.secondaryEffect.isPresent());
        if (this.secondaryEffect.isPresent()) {
            MinecraftTypes.writeVarInt(out, this.secondaryEffect.getAsInt());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
