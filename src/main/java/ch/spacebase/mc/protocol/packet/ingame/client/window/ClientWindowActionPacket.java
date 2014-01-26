package ch.spacebase.mc.protocol.packet.ingame.client.window;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.ItemStack;
import ch.spacebase.mc.protocol.data.game.values.ClickItemParam;
import ch.spacebase.mc.protocol.data.game.values.CreativeGrabParam;
import ch.spacebase.mc.protocol.data.game.values.DropItemParam;
import ch.spacebase.mc.protocol.data.game.values.FillStackParam;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.MoveToHotbarParam;
import ch.spacebase.mc.protocol.data.game.values.ShiftClickItemParam;
import ch.spacebase.mc.protocol.data.game.values.SpreadItemParam;
import ch.spacebase.mc.protocol.data.game.values.WindowAction;
import ch.spacebase.mc.protocol.data.game.values.WindowActionParam;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientWindowActionPacket implements Packet {
	
	private int windowId;
	private int slot;
	private WindowActionParam param;
	private int actionId;
	private WindowAction action;
	private ItemStack clicked;
	
	@SuppressWarnings("unused")
	private ClientWindowActionPacket() {
	}
	
	public ClientWindowActionPacket(int windowId, int actionId, int slot, ItemStack clicked, WindowAction action, WindowActionParam param) {
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
	
	public WindowAction getAction() {
		return this.action;
	}
	
	public WindowActionParam getParam() {
		return this.param;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readByte();
		this.slot = in.readShort();
		byte param = in.readByte();
		this.actionId = in.readShort();
		this.action = MagicValues.key(WindowAction.class, in.readByte());
		this.clicked = NetUtil.readItem(in);
		if(this.action == WindowAction.CLICK_ITEM) {
			this.param = MagicValues.key(ClickItemParam.class, param);
		} else if(this.action == WindowAction.SHIFT_CLICK_ITEM) {
			this.param = MagicValues.key(ShiftClickItemParam.class, param);
		} else if(this.action == WindowAction.MOVE_TO_HOTBAR_SLOT) {
			this.param = MagicValues.key(MoveToHotbarParam.class, param);
		} else if(this.action == WindowAction.CREATIVE_GRAB_MAX_STACK) {
			this.param = MagicValues.key(CreativeGrabParam.class, param);
		} else if(this.action == WindowAction.DROP_ITEM) {
			this.param = MagicValues.key(DropItemParam.class, param + (this.slot != -999 ? 2 : 0));
		} else if(this.action == WindowAction.SPREAD_ITEM) {
			this.param = MagicValues.key(SpreadItemParam.class, param);
		} else if(this.action == WindowAction.FILL_STACK) {
			this.param = MagicValues.key(FillStackParam.class, param);
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeShort(this.slot);
		int param = 0;
		if(this.action == WindowAction.CLICK_ITEM) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		} else if(this.action == WindowAction.SHIFT_CLICK_ITEM) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		} else if(this.action == WindowAction.MOVE_TO_HOTBAR_SLOT) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		} else if(this.action == WindowAction.CREATIVE_GRAB_MAX_STACK) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		} else if(this.action == WindowAction.DROP_ITEM) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param) + (this.slot != -999 ? 2 : 0);
		} else if(this.action == WindowAction.SPREAD_ITEM) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		} else if(this.action == WindowAction.FILL_STACK) {
			param = MagicValues.value(Integer.class, (Enum<?>) this.param);
		}
		
		out.writeByte(param);
		out.writeShort(this.actionId);
		out.writeByte(MagicValues.value(Integer.class, this.action));
		NetUtil.writeItem(out, this.clicked);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
