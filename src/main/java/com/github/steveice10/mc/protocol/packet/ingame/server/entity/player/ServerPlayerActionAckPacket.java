package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerPlayerActionAckPacket extends MinecraftPacket {
    private PlayerAction action;
    private boolean successful;
    private Position position;
    private BlockState newState;

    @SuppressWarnings("unused")
    private ServerPlayerActionAckPacket() {
    }

    public ServerPlayerActionAckPacket(PlayerAction action, boolean successful, Position position, BlockState newState) {
        this.position = position;
        this.newState = newState;
        this.action = action;
        this.successful = successful;
    }

    public PlayerAction getAction() {
        return this.action;
    }

    public boolean wasSuccessful() {
        return this.successful;
    }

    public Position getPosition() {
        return this.position;
    }

    public BlockState getNewState() {
        return this.newState;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.newState = NetUtil.readBlockState(in);
        this.action = MagicValues.key(PlayerAction.class, in.readVarInt());
        this.successful = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        NetUtil.writeBlockState(out, this.newState);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeBoolean(this.successful);
    }
}
