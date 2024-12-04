package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ClickItemAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerActionType;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.CreativeGrabAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.DropItemAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.FillStackAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.MoveToHotbarAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ShiftClickItemAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.SpreadItemAction;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

import java.util.Map;

@Data
@With
public class ServerboundContainerClickPacket implements MinecraftPacket {
    public static final int CLICK_OUTSIDE_NOT_HOLDING_SLOT = -999;

    private final int containerId;
    private final int stateId;
    private final int slot;
    private final @NonNull ContainerActionType action;
    private final @NonNull ContainerAction param;
    private final @Nullable ItemStack carriedItem;
    private final @NonNull Int2ObjectMap<@Nullable ItemStack> changedSlots;

    public ServerboundContainerClickPacket(int containerId, int stateId, int slot,
                                           @NonNull ContainerActionType action, @NonNull ContainerAction param,
                                           @Nullable ItemStack carriedItem, @NonNull Map<Integer, @Nullable ItemStack> changedSlots) {
        this(containerId, stateId, slot, action, param, carriedItem, new Int2ObjectOpenHashMap<>(changedSlots));
    }

    public ServerboundContainerClickPacket(int containerId, int stateId, int slot,
                                           @NonNull ContainerActionType action, @NonNull ContainerAction param,
                                           @Nullable ItemStack carriedItem, @NonNull Int2ObjectMap<@Nullable ItemStack> changedSlots) {
        if ((param == DropItemAction.LEFT_CLICK_OUTSIDE_NOT_HOLDING || param == DropItemAction.RIGHT_CLICK_OUTSIDE_NOT_HOLDING)
                && slot != -CLICK_OUTSIDE_NOT_HOLDING_SLOT) {
            throw new IllegalArgumentException("Slot must be " + CLICK_OUTSIDE_NOT_HOLDING_SLOT
                    + " with param LEFT_CLICK_OUTSIDE_NOT_HOLDING or RIGHT_CLICK_OUTSIDE_NOT_HOLDING");
        }

        this.containerId = containerId;
        this.stateId = stateId;
        this.slot = slot;
        this.action = action;
        this.param = param;
        this.carriedItem = carriedItem;
        this.changedSlots = changedSlots;
    }

    public ServerboundContainerClickPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = helper.readVarInt(in);
        this.stateId = helper.readVarInt(in);
        this.slot = in.readShort();
        byte param = in.readByte();
        this.action = ContainerActionType.from(in.readByte());
        if (this.action == ContainerActionType.CLICK_ITEM) {
            this.param = ClickItemAction.from(param);
        } else if (this.action == ContainerActionType.SHIFT_CLICK_ITEM) {
            this.param = ShiftClickItemAction.from(param);
        } else if (this.action == ContainerActionType.MOVE_TO_HOTBAR_SLOT) {
            this.param = MoveToHotbarAction.from(param);
        } else if (this.action == ContainerActionType.CREATIVE_GRAB_MAX_STACK) {
            this.param = CreativeGrabAction.from(param);
        } else if (this.action == ContainerActionType.DROP_ITEM) {
            this.param = DropItemAction.from(param + (this.slot != -999 ? 2 : 0));
        } else if (this.action == ContainerActionType.SPREAD_ITEM) {
            this.param = SpreadItemAction.from(param);
        } else if (this.action == ContainerActionType.FILL_STACK) {
            this.param = FillStackAction.from(param);
        } else {
            throw new IllegalStateException();
        }

        int changedItemsSize = helper.readVarInt(in);
        this.changedSlots = new Int2ObjectOpenHashMap<>(changedItemsSize);
        for (int i = 0; i < changedItemsSize; i++) {
            int key = in.readShort();
            ItemStack value = helper.readOptionalItemStack(in);
            this.changedSlots.put(key, value);
        }

        this.carriedItem = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.containerId);
        helper.writeVarInt(out, this.stateId);
        out.writeShort(this.slot);

        int param = this.param.getId();
        if (this.action == ContainerActionType.DROP_ITEM) {
            param %= 2;
        }

        out.writeByte(param);
        out.writeByte(this.action.ordinal());

        helper.writeVarInt(out, this.changedSlots.size());
        for (Int2ObjectMap.Entry<ItemStack> pair : this.changedSlots.int2ObjectEntrySet()) {
            out.writeShort(pair.getIntKey());
            helper.writeOptionalItemStack(out, pair.getValue());
        }

        helper.writeOptionalItemStack(out, this.carriedItem);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
