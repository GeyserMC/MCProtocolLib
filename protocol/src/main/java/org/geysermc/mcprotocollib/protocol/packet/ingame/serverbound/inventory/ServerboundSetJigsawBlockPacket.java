package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundSetJigsawBlockPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull Key name;
    private final @NonNull Key target;
    private final @NonNull Key pool;
    private final @NonNull String finalState;
    private final @NonNull String jointType;
    private final int selectionPriority;
    private final int placementPriority;

    public ServerboundSetJigsawBlockPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.name = helper.readResourceLocation(in);
        this.target = helper.readResourceLocation(in);
        this.pool = helper.readResourceLocation(in);
        this.finalState = helper.readString(in);
        this.jointType = helper.readString(in);
        this.selectionPriority = helper.readVarInt(in);
        this.placementPriority = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        helper.writeResourceLocation(out, this.name);
        helper.writeResourceLocation(out, this.target);
        helper.writeResourceLocation(out, this.pool);
        helper.writeString(out, this.finalState);
        helper.writeString(out, this.jointType);
        helper.writeVarInt(out, this.selectionPriority);
        helper.writeVarInt(out, this.placementPriority);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
