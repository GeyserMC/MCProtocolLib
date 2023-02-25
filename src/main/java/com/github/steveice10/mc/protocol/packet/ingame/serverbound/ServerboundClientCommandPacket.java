package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.ClientCommand;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundClientCommandPacket implements MinecraftPacket {
    private final @NonNull ClientCommand request;

    public ServerboundClientCommandPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.request = ClientCommand.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.request.ordinal());
    }
}
