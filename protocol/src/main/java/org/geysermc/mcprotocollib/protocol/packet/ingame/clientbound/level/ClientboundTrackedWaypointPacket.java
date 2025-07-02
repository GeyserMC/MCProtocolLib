package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.AzimuthWaypointData;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.ChunkWaypointData;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.TrackedWaypoint;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.Vec3iWaypointData;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.WaypointData;
import org.geysermc.mcprotocollib.protocol.data.game.level.waypoint.WaypointOperation;

import java.util.Optional;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundTrackedWaypointPacket implements MinecraftPacket {
    private WaypointOperation operation;
    private TrackedWaypoint waypoint;

    public ClientboundTrackedWaypointPacket(ByteBuf in) {
        this.operation = WaypointOperation.from(MinecraftTypes.readVarInt(in));

        UUID uuid = null;
        String id = null;
        if (in.readBoolean()) {
            uuid = MinecraftTypes.readUUID(in);
        } else {
            id = MinecraftTypes.readString(in);
        }

        Key style = MinecraftTypes.readResourceLocation(in);
        Optional<Integer> rgbColor = Optional.ofNullable(MinecraftTypes.readNullable(in, buf -> {
            return 0xFF << 24 | (buf.readByte() & 0xFF) << 16 | (buf.readByte() & 0xFF) << 8 | buf.readByte();
        }));
        TrackedWaypoint.Icon icon = new TrackedWaypoint.Icon(style, rgbColor);

        TrackedWaypoint.Type type = TrackedWaypoint.Type.from(MinecraftTypes.readVarInt(in));

        WaypointData data = switch (type) {
            case EMPTY -> null;
            case VEC3I -> new Vec3iWaypointData(MinecraftTypes.readVec3i(in));
            case CHUNK -> new ChunkWaypointData(MinecraftTypes.readVarInt(in), MinecraftTypes.readVarInt(in));
            case AZIMUTH -> new AzimuthWaypointData(in.readFloat());
        };

        this.waypoint = new TrackedWaypoint(uuid, id, icon, type, data);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.operation.ordinal());

        if (this.waypoint.uuid() != null) {
            out.writeBoolean(true);
            MinecraftTypes.writeUUID(out, this.waypoint.uuid());
        } else {
            out.writeBoolean(false);
            MinecraftTypes.writeString(out, this.waypoint.id());
        }

        MinecraftTypes.writeResourceLocation(out, this.waypoint.icon().style());
        if (this.waypoint.icon().color().isPresent()) {
            out.writeBoolean(true);
            int color = this.waypoint.icon().color().get();
            out.writeByte(color >> 16 & 0xFF);
            out.writeByte(color >> 8 & 0xFF);
            out.writeByte(color & 0xFF);
        } else {
            out.writeBoolean(false);
        }

        MinecraftTypes.writeVarInt(out, this.waypoint.type().ordinal());

        if (this.waypoint.data() instanceof Vec3iWaypointData vec3iData) {
            MinecraftTypes.writeVec3i(out, vec3iData.vector());
        } else if (this.waypoint.data() instanceof ChunkWaypointData chunkData) {
            MinecraftTypes.writeVarInt(out, chunkData.chunkX());
            MinecraftTypes.writeVarInt(out, chunkData.chunkZ());
        } else if (this.waypoint.data() instanceof AzimuthWaypointData azimuthData) {
            out.writeFloat(azimuthData.angle());
        }
    }
}
