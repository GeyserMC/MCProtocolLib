package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundServerDataPacket implements MinecraftPacket {
    private final Component motd;
    private final byte @Nullable[] iconBytes;
    private final boolean enforcesSecureChat;

    public ClientboundServerDataPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.motd = helper.readComponent(in);
        this.iconBytes = helper.readNullable(in, helper::readByteArray);
        this.enforcesSecureChat = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.motd);
        helper.writeNullable(out, this.iconBytes, helper::writeByteArray);
        out.writeBoolean(this.enforcesSecureChat);
    }
}
