package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.chat.BuiltinChatType;
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
public class ClientboundDisguisedChatPacket implements MinecraftPacket {
    private final Component message;
    /**
     * Is {@link BuiltinChatType} defined in the order sent by the server in the login packet.
     */
    private final int chatType;
    private final Component name;
    private final @Nullable Component targetName;


    public ClientboundDisguisedChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.message = helper.readComponent(in);
        this.chatType = helper.readVarInt(in);
        this.name = helper.readComponent(in);
        this.targetName = helper.readNullable(in, helper::readComponent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.message);
        helper.writeVarInt(out, this.chatType);
        helper.writeComponent(out, this.name);
        helper.writeNullable(out, this.targetName, helper::writeComponent);
    }
}
