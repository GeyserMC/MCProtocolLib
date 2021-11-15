package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundAddPaintingPacket implements Packet {
    private final int entityId;
    private final @NonNull UUID uuid;
    private final @NonNull PaintingType paintingType;
    private final @NonNull Position position;
    private final @NonNull Direction direction;

    public ClientboundAddPaintingPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.paintingType = MagicValues.key(PaintingType.class, in.readVarInt());
        this.position = Position.read(in);
        this.direction = Direction.getByHorizontalIndex(in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeVarInt(MagicValues.value(Integer.class, this.paintingType));
        Position.write(out, this.position);
        out.writeByte(this.direction.getHorizontalIndex());
    }
}
