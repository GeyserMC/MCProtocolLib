package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.particle.*;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnParticlePacket implements Packet {

	private static final String ITEM_BREAK_PREFIX = "iconcrack_";
	private static final String BLOCK_BREAK_PREFIX = "blockcrack_";
	private static final String BLOCK_IMPACT_PREFIX = "blockdust_";

	private Particle particle;
	private float x;
	private float y;
	private float z;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private float velocityOffset;
	private int amount;

	@SuppressWarnings("unused")
	private ServerSpawnParticlePacket() {
	}

	public ServerSpawnParticlePacket(Particle particle, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float velocityOffset, int amount) {
		this.particle = particle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.velocityOffset = velocityOffset;
		this.amount = amount;
	}

	public Particle getParticle() {
		return this.particle;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public float getOffsetX() {
		return this.offsetX;
	}

	public float getOffsetY() {
		return this.offsetY;
	}

	public float getOffsetZ() {
		return this.offsetZ;
	}

	public float getVelocityOffset() {
		return this.velocityOffset;
	}

	public int getAmount() {
		return this.amount;
	}

	@Override
	public void read(NetInput in) throws IOException {
		String value = in.readString();
		if(value.startsWith(ITEM_BREAK_PREFIX)) {
			String parts[] = value.split("_");
			int id = Integer.parseInt(parts[1]);
			int data = parts.length > 2 ? Integer.parseInt(parts[2]) : -1;
			this.particle = new ItemBreakParticle(id, data);
		} else if(value.startsWith(BLOCK_BREAK_PREFIX)) {
			String parts[] = value.split("_");
			int id = Integer.parseInt(parts[1]);
			int data = parts.length > 2 ? Integer.parseInt(parts[2]) : -1;
			this.particle = new BlockBreakParticle(id, data);
		} else if(value.startsWith(BLOCK_IMPACT_PREFIX)) {
			String parts[] = value.split("_");
			int id = Integer.parseInt(parts[1]);
			int data = parts.length > 2 ? Integer.parseInt(parts[2]) : -1;
			this.particle = new BlockImpactParticle(id, data);
		} else {
			this.particle = MagicValues.key(GenericParticle.class, value);
		}

		this.x = in.readFloat();
		this.y = in.readFloat();
		this.z = in.readFloat();
		this.offsetX = in.readFloat();
		this.offsetY = in.readFloat();
		this.offsetZ = in.readFloat();
		this.velocityOffset = in.readFloat();
		this.amount = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		String value = "";
		if(this.particle instanceof ItemBreakParticle) {
			ItemBreakParticle particle = (ItemBreakParticle) this.particle;
			value = ITEM_BREAK_PREFIX + particle.getId() + (particle.getData() != -1 ? "_" + particle.getData() : "");
		} else if(this.particle instanceof BlockBreakParticle) {
			BlockBreakParticle particle = (BlockBreakParticle) this.particle;
			value = BLOCK_BREAK_PREFIX + particle.getId() + (particle.getData() != -1 ? "_" + particle.getData() : "");
		} else if(this.particle instanceof BlockImpactParticle) {
			BlockImpactParticle particle = (BlockImpactParticle) this.particle;
			value = BLOCK_IMPACT_PREFIX + particle.getId() + (particle.getData() != -1 ? "_" + particle.getData() : "");
		} else if(this.particle instanceof GenericParticle) {
			value = MagicValues.value(String.class, (GenericParticle) this.particle);
		}

		out.writeString(value);
		out.writeFloat(this.x);
		out.writeFloat(this.y);
		out.writeFloat(this.z);
		out.writeFloat(this.offsetX);
		out.writeFloat(this.offsetY);
		out.writeFloat(this.offsetZ);
		out.writeFloat(this.velocityOffset);
		out.writeInt(this.amount);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
