package ch.spacebase.mc.protocol.packet.ingame.client.window;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.ItemStack;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientWindowActionPacket implements Packet {
	
	private int windowId;
	private int slot;
	private ActionParam param;
	private int actionId;
	private Action action;
	private ItemStack clicked;
	
	@SuppressWarnings("unused")
	private ClientWindowActionPacket() {
	}
	
	public ClientWindowActionPacket(int windowId, int actionId, int slot, ItemStack clicked, Action action, ActionParam param) {
		this.windowId = windowId;
		this.actionId = actionId;
		this.slot = slot;
		this.clicked = clicked;
		this.action = action;
		this.param = param;
	}
	
	public int getWindowId() {
		return this.windowId;
	}
	
	public int getActionId() {
		return this.actionId;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public ItemStack getClickedItem() {
		return this.clicked;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public ActionParam getParam() {
		return this.param;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readByte();
		this.slot = in.readShort();
		byte param = in.readByte();
		this.actionId = in.readShort();
		byte id = in.readByte();
		this.action = Action.values()[id];
		this.clicked = NetUtil.readItem(in);
		this.param = this.valueToParam(param);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeShort(this.slot);
		out.writeByte(this.paramToValue(this.param));
		out.writeShort(this.actionId);
		out.writeByte(this.action.ordinal());
		NetUtil.writeItem(out, this.clicked);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private byte paramToValue(ActionParam param) throws IOException {
		if(param == ClickItemParam.LEFT_CLICK) {
			return 0;
		} else if(param == ClickItemParam.RIGHT_CLICK) {
			return 1;
		}
		
		if(param == ShiftClickItemParam.LEFT_CLICK) {
			return 0;
		} else if(param == ShiftClickItemParam.RIGHT_CLICK) {
			return 1;
		}
		
		if(param == MoveToHotbarParam.SLOT_1) {
			return 0;
		} else if(param == MoveToHotbarParam.SLOT_2) {
			return 1;
		} else if(param == MoveToHotbarParam.SLOT_3) {
			return 2;
		} else if(param == MoveToHotbarParam.SLOT_4) {
			return 3;
		} else if(param == MoveToHotbarParam.SLOT_5) {
			return 4;
		} else if(param == MoveToHotbarParam.SLOT_6) {
			return 5;
		} else if(param == MoveToHotbarParam.SLOT_7) {
			return 6;
		} else if(param == MoveToHotbarParam.SLOT_8) {
			return 7;
		} else if(param == MoveToHotbarParam.SLOT_9) {
			return 8;
		}
		
		if(param == CreativeGrabParam.GRAB) {
			return 2;
		}
		
		if(param == DropItemParam.DROP_FROM_SELECTED) {
			return 0;
		} else if(param == DropItemParam.DROP_SELECTED_STACK) {
			return 1;
		} else if(param == DropItemParam.LEFT_CLICK_OUTSIDE_NOT_HOLDING) {
			return 0;
		} else if(param == DropItemParam.RIGHT_CLICK_OUTSIDE_NOT_HOLDING) {
			return 1;
		}
		
		if(param == SpreadItemParam.LEFT_MOUSE_BEGIN_DRAG) {
			return 0;
		} else if(param == SpreadItemParam.LEFT_MOUSE_ADD_SLOT) {
			return 1;
		} else if(param == SpreadItemParam.LEFT_MOUSE_END_DRAG) {
			return 2;
		} else if(param == SpreadItemParam.RIGHT_MOUSE_BEGIN_DRAG) {
			return 4;
		} else if(param == SpreadItemParam.RIGHT_MOUSE_ADD_SLOT) {
			return 5;
		} else if(param == SpreadItemParam.RIGHT_MOUSE_END_DRAG) {
			return 6;
		}
		
		if(param == FillStackParam.FILL) {
			return 0;
		}
		
		throw new IOException("Unmapped action param: " + param);
	}
	
	private ActionParam valueToParam(byte value) throws IOException {
		if(this.action == Action.CLICK_ITEM) {
			if(value == 0) {
				return ClickItemParam.LEFT_CLICK;
			} else if(value == 1) {
				return ClickItemParam.RIGHT_CLICK;
			}
		}
		
		if(this.action == Action.SHIFT_CLICK_ITEM) {
			if(value == 0) {
				return ShiftClickItemParam.LEFT_CLICK;
			} else if(value == 1) {
				return ShiftClickItemParam.RIGHT_CLICK;
			}
		}
		
		if(this.action == Action.MOVE_TO_HOTBAR_SLOT) {
			if(value == 0) {
				return MoveToHotbarParam.SLOT_1;
			} else if(value == 1) {
				return MoveToHotbarParam.SLOT_2;
			} else if(value == 2) {
				return MoveToHotbarParam.SLOT_3;
			} else if(value == 3) {
				return MoveToHotbarParam.SLOT_4;
			} else if(value == 4) {
				return MoveToHotbarParam.SLOT_5;
			} else if(value == 5) {
				return MoveToHotbarParam.SLOT_6;
			} else if(value == 6) {
				return MoveToHotbarParam.SLOT_7;
			} else if(value == 7) {
				return MoveToHotbarParam.SLOT_8;
			} else if(value == 8) {
				return MoveToHotbarParam.SLOT_9;
			}
		}
		
		if(this.action == Action.CREATIVE_GRAB_MAX_STACK) {
			if(value == 2) {
				return CreativeGrabParam.GRAB;
			}
		}
		
		if(this.action == Action.DROP_ITEM) {
			if(this.slot == -999) {
				if(value == 0) {
					return DropItemParam.LEFT_CLICK_OUTSIDE_NOT_HOLDING;
				} else if(value == 1) {
					return DropItemParam.RIGHT_CLICK_OUTSIDE_NOT_HOLDING;
				}
			} else {
				if(value == 0) {
					return DropItemParam.DROP_FROM_SELECTED;
				} else if(value == 1) {
					return DropItemParam.DROP_SELECTED_STACK;
				}
			}
		}
		
		if(this.action == Action.SPREAD_ITEM) {
			if(value == 0) {
				return SpreadItemParam.LEFT_MOUSE_BEGIN_DRAG;
			} else if(value == 1) {
				return SpreadItemParam.LEFT_MOUSE_ADD_SLOT;
			} else if(value == 2) {
				return SpreadItemParam.LEFT_MOUSE_END_DRAG;
			} else if(value == 4) {
				return SpreadItemParam.RIGHT_MOUSE_BEGIN_DRAG;
			} else if(value == 5) {
				return SpreadItemParam.RIGHT_MOUSE_ADD_SLOT;
			} else if(value == 6) {
				return SpreadItemParam.RIGHT_MOUSE_END_DRAG;
			}
		}
		
		if(this.action == Action.FILL_STACK) {
			if(value == 0) {
				return FillStackParam.FILL;
			}
		}
		
		throw new IOException("Unknown action param value: " + value);
	}
	
	public static enum Action {
		CLICK_ITEM,
		SHIFT_CLICK_ITEM,
		MOVE_TO_HOTBAR_SLOT,
		CREATIVE_GRAB_MAX_STACK,
		DROP_ITEM,
		SPREAD_ITEM,
		FILL_STACK;
	}
	
	public static interface ActionParam {
	}
	
	public static enum ClickItemParam implements ActionParam {
		LEFT_CLICK,
		RIGHT_CLICK;
	}
	
	public static enum ShiftClickItemParam implements ActionParam {
		LEFT_CLICK,
		RIGHT_CLICK;
	}
	
	public static enum MoveToHotbarParam implements ActionParam {
		SLOT_1,
		SLOT_2,
		SLOT_3,
		SLOT_4,
		SLOT_5,
		SLOT_6,
		SLOT_7,
		SLOT_8,
		SLOT_9;
	}
	
	public static enum CreativeGrabParam implements ActionParam {
		GRAB;
	}
	
	public static enum DropItemParam implements ActionParam {
		DROP_FROM_SELECTED,
		DROP_SELECTED_STACK,
		LEFT_CLICK_OUTSIDE_NOT_HOLDING,
		RIGHT_CLICK_OUTSIDE_NOT_HOLDING;
	}
	
	public static enum SpreadItemParam implements ActionParam {
		LEFT_MOUSE_BEGIN_DRAG,
		LEFT_MOUSE_ADD_SLOT,
		LEFT_MOUSE_END_DRAG,
		RIGHT_MOUSE_BEGIN_DRAG,
		RIGHT_MOUSE_ADD_SLOT,
		RIGHT_MOUSE_END_DRAG;
	}
	
	public static enum FillStackParam implements ActionParam {
		FILL;
	}

}
