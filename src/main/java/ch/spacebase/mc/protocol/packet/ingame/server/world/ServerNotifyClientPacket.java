package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerNotifyClientPacket implements Packet {
	
	private Notification notification;
	private NotificationValue value;
	
	@SuppressWarnings("unused")
	private ServerNotifyClientPacket() {
	}
	
	public ServerNotifyClientPacket(Notification notification, NotificationValue value) {
		this.value = value;
	}
	
	public Notification getNotification() {
		return this.notification;
	}
	
	public NotificationValue getValue() {
		return this.value;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.notification = Notification.values()[in.readUnsignedByte()];
		this.value = this.floatToValue(in.readFloat());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.notification.ordinal());
		out.writeFloat(this.valueToFloat(this.value));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private NotificationValue floatToValue(float f) {
		if(this.notification == Notification.CHANGE_GAMEMODE) {
			if(f == 0) {
				return GameModeValue.SURVIVAL;
			} else if(f == 1) {
				return GameModeValue.CREATIVE;
			} else if(f == 2) {
				return GameModeValue.ADVENTURE;
			}
		} else if(this.notification == Notification.DEMO_MESSAGE) {
			if(f == 0) {
				return DemoMessageValue.WELCOME;
			} else if(f == 101) {
				return DemoMessageValue.MOVEMENT_CONTROLS;
			} else if(f == 102) {
				return DemoMessageValue.JUMP_CONTROL;
			} else if(f == 103) {
				return DemoMessageValue.INVENTORY_CONTROL;
			}
		} else if(this.notification == Notification.RAIN_STRENGTH) {
			return new RainStrengthValue((int) f);
		} else if(this.notification == Notification.THUNDER_STRENGTH) {
			return new ThunderStrengthValue((int) f);
		}
		
		return null;
	}
	
	private float valueToFloat(NotificationValue value) {
		if(value == GameModeValue.SURVIVAL) {
			return 0;
		} else if(value == GameModeValue.CREATIVE) {
			return 1;
		} else if(value == GameModeValue.ADVENTURE) {
			return 2;
		}
		
		if(value == DemoMessageValue.WELCOME) {
			return 0;
		} else if(value == DemoMessageValue.MOVEMENT_CONTROLS) {
			return 101;
		} else if(value == DemoMessageValue.JUMP_CONTROL) {
			return 102;
		} else if(value == DemoMessageValue.INVENTORY_CONTROL) {
			return 103;
		}
		
		if(value instanceof RainStrengthValue) {
			return ((RainStrengthValue) value).getStrength();
		}
		
		if(value instanceof ThunderStrengthValue) {
			return ((ThunderStrengthValue) value).getStrength();
		}
		
		return 0;
	}
	
	public static enum Notification {
		INVALID_BED,
		START_RAIN,
		STOP_RAIN,
		CHANGE_GAMEMODE,
		ENTER_CREDITS,
		DEMO_MESSAGE,
		ARROW_HIT_PLAYER,
		RAIN_STRENGTH,
		THUNDER_STRENGTH;
	}
	
	public static interface NotificationValue {
	}
	
	public static enum GameModeValue implements NotificationValue {
		SURVIVAL,
		CREATIVE,
		ADVENTURE;
	}
	
	public static enum DemoMessageValue implements NotificationValue {
		WELCOME,
		MOVEMENT_CONTROLS,
		JUMP_CONTROL,
		INVENTORY_CONTROL;
	}
	
	public static class RainStrengthValue implements NotificationValue {
		private float strength;
		
		public RainStrengthValue(float strength) {
			if(strength > 1) {
				strength = 1;
			}
			
			if(strength < 0) {
				strength = 0;
			}
			
			this.strength = strength;
		}
		
		public float getStrength() {
			return this.strength;
		}
	}
	
	public static class ThunderStrengthValue implements NotificationValue {
		private float strength;
		
		public ThunderStrengthValue(float strength) {
			if(strength > 1) {
				strength = 1;
			}
			
			if(strength < 0) {
				strength = 0;
			}
			
			this.strength = strength;
		}
		
		public float getStrength() {
			return this.strength;
		}
	}

}
