package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chat.BuiltinChatType;

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


    public ClientboundDisguisedChatPacket(MinecraftByteBuf buf) {
        this.message = buf.readComponent();
        this.chatType = buf.readVarInt();
        this.name = buf.readComponent();
        this.targetName = buf.readNullable(buf::readComponent);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeComponent(this.message);
        buf.writeVarInt(this.chatType);
        buf.writeComponent(this.name);
        buf.writeNullable(this.targetName, buf::writeComponent);
    }
}
