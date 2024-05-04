package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerType;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenScreenPacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull ContainerType type;
    private final @NonNull Component title;

    public ClientboundOpenScreenPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readVarInt();
        this.type = ContainerType.from(buf.readVarInt());
        this.title = buf.readComponent();
    }

    @Deprecated
    public ClientboundOpenScreenPacket(int containerId, @NonNull ContainerType type, @NonNull String name) {
        this.containerId = containerId;
        this.type = type;
        this.title = DefaultComponentSerializer.get().deserialize(name);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.containerId);
        buf.writeVarInt(this.type.ordinal());
        buf.writeComponent(this.title);
    }

    @Deprecated
    public String getName() {
        return DefaultComponentSerializer.get().serialize(title);
    }

    @Deprecated
    public ClientboundOpenScreenPacket withName(String name) {
        return new ClientboundOpenScreenPacket(this.containerId, this.type, DefaultComponentSerializer.get().deserialize(name));
    }
}
