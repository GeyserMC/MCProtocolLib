package org.spacehq.mc.protocol.data.game;

public class Rotation {
	private float pitch;
	private float yaw;
	private float roll;

	public Rotation() {
		this(0, 0, 0);
	}

	public Rotation(float pitch, float yaw, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getRoll() {
		return this.roll;
	}
}
