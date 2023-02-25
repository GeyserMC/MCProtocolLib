package com.github.steveice10.mc.protocol.packet.ingame.clientbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenScreenPacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull ContainerType type;
    private final @NonNull Component title;

    public ClientboundOpenScreenPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.containerId = helper.readVarInt(in);
        this.type = ContainerType.from(helper.readVarInt(in));
        this.title = helper.readComponent(in);
    }

    @Deprecated
    public ClientboundOpenScreenPacket(int containerId, @NonNull ContainerType type, @NonNull String name) {
        this.containerId = containerId;
        this.type = type;
        this.title = DefaultComponentSerializer.get().deserialize(name);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.containerId);
        helper.writeVarInt(out, this.type.ordinal());
        helper.writeComponent(out, this.title);
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
