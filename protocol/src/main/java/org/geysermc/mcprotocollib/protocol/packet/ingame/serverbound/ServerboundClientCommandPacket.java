package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;

@Data
@With
@AllArgsConstructor
public class ServerboundClientCommandPacket implements MinecraftPacket {
    private final @NonNull ClientCommand request;

    public ServerboundClientCommandPacket(MinecraftByteBuf buf) {
        this.request = ClientCommand.from(buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.request.ordinal());
    }
}
