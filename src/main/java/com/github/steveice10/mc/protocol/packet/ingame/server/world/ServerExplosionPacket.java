package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.world.block.ExplodedBlockRecord;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerExplosionPacket implements Packet {
    private float x;
    private float y;
    private float z;
    private float radius;
    private @NonNull List<ExplodedBlockRecord> exploded;
    private float pushX;
    private float pushY;
    private float pushZ;

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.radius = in.readFloat();
        this.exploded = new ArrayList<ExplodedBlockRecord>();
        int length = in.readVarInt();
        for(int count = 0; count < length; count++) {
            this.exploded.add(new ExplodedBlockRecord(in.readByte(), in.readByte(), in.readByte()));
        }

        this.pushX = in.readFloat();
        this.pushY = in.readFloat();
        this.pushZ = in.readFloat();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.radius);
        out.writeVarInt(this.exploded.size());
        for(ExplodedBlockRecord record : this.exploded) {
            out.writeByte(record.getX());
            out.writeByte(record.getY());
            out.writeByte(record.getZ());
        }

        out.writeFloat(this.pushX);
        out.writeFloat(this.pushY);
        out.writeFloat(this.pushZ);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
