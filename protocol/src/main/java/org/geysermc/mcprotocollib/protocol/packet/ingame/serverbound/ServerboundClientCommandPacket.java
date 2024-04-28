package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;

@Data
@With
@AllArgsConstructor
public class ServerboundClientCommandPacket implements MinecraftPacket {
    private final @NonNull ClientCommand request;

    public ServerboundClientCommandPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.request = ClientCommand.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.request.ordinal());
    }
}
