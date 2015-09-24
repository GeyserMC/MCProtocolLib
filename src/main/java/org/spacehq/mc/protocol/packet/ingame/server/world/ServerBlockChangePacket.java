package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.world.block.BlockChangeRecord;
import org.spacehq.mc.protocol.packet.PacketBufferInput;
import org.spacehq.mc.protocol.packet.PacketBufferOutput;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerBlockChangePacket implements Packet {

    private BlockChangeRecord record;

    @SuppressWarnings("unused")
    private ServerBlockChangePacket() {
    }

    public ServerBlockChangePacket(BlockChangeRecord record) {
        this.record = record;
    }

    public BlockChangeRecord getRecord() {
        return this.record;
    }

    @Override
    public void read(PacketBufferInput in) throws IOException {
        Position position = in.readPosition();
        int block = in.readVarInt();

        this.record = new BlockChangeRecord(position, block >> 4, block & 0xF);
    }

    @Override
    public void write(PacketBufferOutput out) throws IOException {
        out.writePosition(this.record.getPosition());
        out.writeVarInt(this.record.getId() << 4 | (this.record.getData() & 0xF));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
