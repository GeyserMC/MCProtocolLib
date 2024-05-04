package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundSetJigsawBlockPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull String name;
    private final @NonNull String target;
    private final @NonNull String pool;
    private final @NonNull String finalState;
    private final @NonNull String jointType;
    private final int selectionPriority;
    private final int placementPriority;

    public ServerboundSetJigsawBlockPacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.name = buf.readString();
        this.target = buf.readString();
        this.pool = buf.readString();
        this.finalState = buf.readString();
        this.jointType = buf.readString();
        this.selectionPriority = buf.readVarInt();
        this.placementPriority = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeString(this.name);
        buf.writeString(this.target);
        buf.writeString(this.pool);
        buf.writeString(this.finalState);
        buf.writeString(this.jointType);
        buf.writeVarInt(this.selectionPriority);
        buf.writeVarInt(this.placementPriority);
    }
}
