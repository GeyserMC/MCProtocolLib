package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NotNull String username;
    private final @NotNull UUID profileId;

    public ServerboundHelloPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.username = helper.readString(in);
        this.profileId = helper.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.username);
        helper.writeUUID(out, this.profileId);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
