package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityStatusPacket implements Packet {
	
	protected int entityId;
	protected Status status;
	
	@SuppressWarnings("unused")
	private ServerEntityStatusPacket() {
	}
	
	public ServerEntityStatusPacket(int entityId, Status status) {
		this.entityId = entityId;
		this.status = status;
	}
	
	public Status getStatus() {
		return this.status;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.status = valueToStatus(in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(statusToValue(this.status));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private static byte statusToValue(Status status) throws IOException {
		switch(status) {
			case HURT_OR_MINECART_SPAWNER_DELAY_RESET:
				return 1;
			case LIVING_HURT:
				return 2;
			case DEAD:
				return 3;
			case IRON_GOLEM_THROW:
				return 4;
			case TAMING:
				return 6;
			case TAMED:
				return 7;
			case WOLF_SHAKING:
				return 8;
			case FINISHED_EATING:
				return 9;
			case SHEEP_GRAZING_OR_TNT_CART_EXPLODING:
				return 10;
			case IRON_GOLEM_ROSE:
				return 11;
			case VILLAGER_HEARTS:
				return 12;
			case VILLAGER_ANGRY:
				return 13;
			case VILLAGER_HAPPY:
				return 14;
			case WITCH_MAGIC_PARTICLES:
				return 15;
			case ZOMBIE_VILLAGER_SHAKING:
				return 16;
			case FIREWORK_EXPLODING:
				return 17;
			case ANIMAL_HEARTS:
				return 18;
			default:
				throw new IOException("Unmapped entity status: " + status);
		}
	}
	
	private static Status valueToStatus(byte value) throws IOException {
		switch(value) {
			case 1:
				return Status.HURT_OR_MINECART_SPAWNER_DELAY_RESET;
			case 2:
				return Status.LIVING_HURT;
			case 3:
				return Status.DEAD;
			case 4:
				return Status.IRON_GOLEM_THROW;
			case 6:
				return Status.TAMING;
			case 7:
				return Status.TAMED;
			case 8:
				return Status.WOLF_SHAKING;
			case 9:
				return Status.FINISHED_EATING;
			case 10:
				return Status.SHEEP_GRAZING_OR_TNT_CART_EXPLODING;
			case 11:
				return Status.IRON_GOLEM_ROSE;
			case 12:
				return Status.VILLAGER_HEARTS;
			case 13:
				return Status.VILLAGER_ANGRY;
			case 14:
				return Status.VILLAGER_HAPPY;
			case 15:
				return Status.WITCH_MAGIC_PARTICLES;
			case 16:
				return Status.ZOMBIE_VILLAGER_SHAKING;
			case 17:
				return Status.FIREWORK_EXPLODING;
			case 18:
				return Status.ANIMAL_HEARTS;
			default:
				throw new IOException("Unknown entity status value: " + value);
		}
	}
	
	public static enum Status {
		HURT_OR_MINECART_SPAWNER_DELAY_RESET,
		LIVING_HURT,
		DEAD,
		IRON_GOLEM_THROW,
		TAMING,
		TAMED,
		WOLF_SHAKING,
		FINISHED_EATING,
		SHEEP_GRAZING_OR_TNT_CART_EXPLODING,
		IRON_GOLEM_ROSE,
		VILLAGER_HEARTS,
		VILLAGER_ANGRY,
		VILLAGER_HAPPY,
		WITCH_MAGIC_PARTICLES,
		ZOMBIE_VILLAGER_SHAKING,
		FIREWORK_EXPLODING,
		ANIMAL_HEARTS;
	}

}
