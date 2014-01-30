package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.entity.player.GameMode;
import ch.spacebase.mc.protocol.data.game.values.setting.Difficulty;
import ch.spacebase.mc.protocol.data.game.values.world.WorldType;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerRespawnPacket implements Packet {
	
	private int dimension;
	private Difficulty difficulty;
	private GameMode gamemode;
	private WorldType worldType;
	
	@SuppressWarnings("unused")
	private ServerRespawnPacket() {
	}
	
	public ServerRespawnPacket(int dimension, Difficulty difficulty, GameMode gamemode, WorldType worldType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.worldType = worldType;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public GameMode getGameMode() {
		return this.gamemode;
	}
	
	public WorldType getWorldType() {
		return this.worldType;
	}

	@Override
	public void read(NetInput in) throws IOException {
        this.dimension = in.readInt();
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldType = MagicValues.key(WorldType.class, in.readString());
        // Unfortunately this is needed to check whether to read skylight values in chunk data packets.
        NetUtil.hasSky = this.dimension != -1 && this.dimension != 1;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.dimension);
		out.writeByte(MagicValues.value(Integer.class, this.difficulty));
		out.writeByte(MagicValues.value(Integer.class, this.gamemode));
		out.writeString(MagicValues.value(String.class, this.worldType));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
