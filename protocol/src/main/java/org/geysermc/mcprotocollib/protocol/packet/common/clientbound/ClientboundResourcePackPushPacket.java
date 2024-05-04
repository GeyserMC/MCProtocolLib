package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPushPacket implements MinecraftPacket {
    private final @NonNull UUID id;
    private final @NonNull String url;
    private final @NonNull String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    public ClientboundResourcePackPushPacket(MinecraftByteBuf buf) {
        this.id = buf.readUUID();
        this.url = buf.readString();
        this.hash = buf.readString();
        this.required = buf.readBoolean();
        this.prompt = buf.readNullable(buf::readComponent);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeString(this.url);
        buf.writeString(this.hash);
        buf.writeBoolean(this.required);
        buf.writeNullable(this.prompt, buf::writeComponent);
    }
}
