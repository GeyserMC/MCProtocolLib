package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundSetJigsawBlockPacket implements MinecraftPacket {
    private final Vector3i position;
    private final Key name;
    private final Key target;
    private final Key pool;
    private final String finalState;
    private final String jointType;
    private final int selectionPriority;
    private final int placementPriority;

    public ServerboundSetJigsawBlockPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.name = MinecraftTypes.readResourceLocation(in);
        this.target = MinecraftTypes.readResourceLocation(in);
        this.pool = MinecraftTypes.readResourceLocation(in);
        this.finalState = MinecraftTypes.readString(in);
        this.jointType = MinecraftTypes.readString(in);
        this.selectionPriority = MinecraftTypes.readVarInt(in);
        this.placementPriority = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        MinecraftTypes.writeResourceLocation(out, this.name);
        MinecraftTypes.writeResourceLocation(out, this.target);
        MinecraftTypes.writeResourceLocation(out, this.pool);
        MinecraftTypes.writeString(out, this.finalState);
        MinecraftTypes.writeString(out, this.jointType);
        MinecraftTypes.writeVarInt(out, this.selectionPriority);
        MinecraftTypes.writeVarInt(out, this.placementPriority);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
