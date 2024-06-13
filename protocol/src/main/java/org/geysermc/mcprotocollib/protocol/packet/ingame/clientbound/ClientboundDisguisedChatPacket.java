package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatType;

@Data
@With
@AllArgsConstructor
public class ClientboundDisguisedChatPacket implements MinecraftPacket {
    private final Component message;
    private final Holder<ChatType> chatType;
    private final Component name;
    private final @Nullable Component targetName;


    public ClientboundDisguisedChatPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.message = helper.readComponent(in);
        this.chatType = helper.readHolder(in, helper::readChatType);
        this.name = helper.readComponent(in);
        this.targetName = helper.readNullable(in, helper::readComponent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.message);
        helper.writeHolder(out, this.chatType, helper::writeChatType);
        helper.writeComponent(out, this.name);
        helper.writeNullable(out, this.targetName, helper::writeComponent);
    }
}
