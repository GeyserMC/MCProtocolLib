package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.block.UpdatedTileType;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerUpdateTileEntityPacket implements Packet {

    private Position position;
    private UpdatedTileType type;
    private CompoundTag nbt;

    @SuppressWarnings("unused")
    private ServerUpdateTileEntityPacket() {
    }

    public ServerUpdateTileEntityPacket(Position position, UpdatedTileType type, CompoundTag nbt) {
        this.position = position;
        this.type = type;
        this.nbt = nbt;
    }

    public Position getPosition() {
        return this.position;
    }

    public UpdatedTileType getType() {
        return this.type;
    }

    public CompoundTag getNBT() {
        return this.nbt;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.type = MagicValues.key(UpdatedTileType.class, in.readUnsignedByte());
        this.nbt = NetUtil.readNBT(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        NetUtil.writeNBT(out, this.nbt);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
