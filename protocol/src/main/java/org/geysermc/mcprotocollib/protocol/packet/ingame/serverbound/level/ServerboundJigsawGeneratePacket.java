package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundJigsawGeneratePacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final int levels;
    private final boolean keepJigsaws;

    public ServerboundJigsawGeneratePacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.levels = buf.readVarInt();
        this.keepJigsaws = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeVarInt(this.levels);
        buf.writeBoolean(this.keepJigsaws);
    }
}
