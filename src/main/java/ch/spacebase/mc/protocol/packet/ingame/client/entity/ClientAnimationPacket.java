package ch.spacebase.mc.protocol.packet.ingame.client.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientAnimationPacket implements Packet {
	
	private int entityId;
	private Animation animation;
	
	public ClientAnimationPacket() {
	}
	
	public ClientAnimationPacket(int entityId, Animation animation) {
		this.entityId = entityId;
		this.animation = animation;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.animation = Animation.decode(in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.animation.encode());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

	public static enum Animation {
		NO_ANIMATION(0),
		SWING_ARM(1),
		DAMAGE_ANIMATION(2),
		LEAVE_BED(3),
		EAT_FOOD(5),
		CRITICAL_EFFECT(6),
		MAGIC_CRITICAL_EFFECT(7),
		UNKNOWN_102(102),
		CROUCH(104),
		UNCROUCH(105);
		
		private static final Map<Integer, Animation> lookup = 
			new HashMap<Integer, Animation>();

		static {
			for (final Animation a : Animation.values()) {
				lookup.put(a.encode(), a);
			}
		}
		
		private final int animationId;

		private Animation(final int animationId) {
			this.animationId = animationId;
		}
		
		public static Animation decode(final int animationId) {
			return lookup.get(animationId);
		}

		private Integer encode() {
			return animationId;
		}
	}
}
