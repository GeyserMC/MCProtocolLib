package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;

@Data
@With
@AllArgsConstructor
public class ServerboundChangeGameModePacket implements MinecraftPacket {
    private final GameMode mode;

    public ServerboundChangeGameModePacket(ByteBuf in) {
        this.mode = GameMode.byId(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.mode.ordinal());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
