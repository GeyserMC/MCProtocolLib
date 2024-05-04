package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSetBeaconPacket implements MinecraftPacket {
    private final OptionalInt primaryEffect;
    private final OptionalInt secondaryEffect;

    public ServerboundSetBeaconPacket(MinecraftByteBuf buf) {
        if (buf.readBoolean()) {
            this.primaryEffect = OptionalInt.of(buf.readVarInt());
        } else {
            this.primaryEffect = OptionalInt.empty();
        }

        if (buf.readBoolean()) {
            this.secondaryEffect = OptionalInt.of(buf.readVarInt());
        } else {
            this.secondaryEffect = OptionalInt.empty();
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.primaryEffect.isPresent());
        if (this.primaryEffect.isPresent()) {
            buf.writeVarInt(this.primaryEffect.getAsInt());
        }

        buf.writeBoolean(this.secondaryEffect.isPresent());
        if (this.secondaryEffect.isPresent()) {
            buf.writeVarInt(this.secondaryEffect.getAsInt());
        }
    }
}
