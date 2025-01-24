package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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


    public ClientboundDisguisedChatPacket(ByteBuf in) {
        this.message = MinecraftTypes.readComponent(in);
        this.chatType = MinecraftTypes.readHolder(in, MinecraftTypes::readChatType);
        this.name = MinecraftTypes.readComponent(in);
        this.targetName = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.message);
        MinecraftTypes.writeHolder(out, this.chatType, MinecraftTypes::writeChatType);
        MinecraftTypes.writeComponent(out, this.name);
        MinecraftTypes.writeNullable(out, this.targetName, MinecraftTypes::writeComponent);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
