package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCommandMinecartPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull String command;
    private final boolean doesTrackOutput;

    public ServerboundSetCommandMinecartPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.command = MinecraftTypes.readString(in);
        this.doesTrackOutput = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeString(out, this.command);
        out.writeBoolean(this.doesTrackOutput);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
