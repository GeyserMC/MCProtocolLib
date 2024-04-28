package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundHelloPacket implements MinecraftPacket {
    private final @NonNull String username;
    private final @NonNull UUID profileId;

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
