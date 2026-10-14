package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPushPacket implements MinecraftPacket {
    private final UUID id;
    private final String url;
    private final String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    public ClientboundResourcePackPushPacket(ByteBuf in) {
        this.id = MinecraftTypes.readUUID(in);
        this.url = MinecraftTypes.readString(in);
        this.hash = MinecraftTypes.readString(in);
        this.required = in.readBoolean();
        this.prompt = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, this.id);
        MinecraftTypes.writeString(out, this.url);
        MinecraftTypes.writeString(out, this.hash);
        out.writeBoolean(this.required);
        MinecraftTypes.writeNullable(out, this.prompt, MinecraftTypes::writeComponent);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
