package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import java.io.IOException;

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

    public ServerboundSetJigsawBlockPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.position = helper.readPosition(in);
        this.name = helper.readString(in);
        this.target = helper.readString(in);
        this.pool = helper.readString(in);
        this.finalState = helper.readString(in);
        this.jointType = helper.readString(in);
        this.selectionPriority = helper.readVarInt(in);
        this.placementPriority = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writePosition(out, this.position);
        helper.writeString(out, this.name);
        helper.writeString(out, this.target);
        helper.writeString(out, this.pool);
        helper.writeString(out, this.finalState);
        helper.writeString(out, this.jointType);
        helper.writeVarInt(out, this.selectionPriority);
        helper.writeVarInt(out, this.placementPriority);
    }
}
