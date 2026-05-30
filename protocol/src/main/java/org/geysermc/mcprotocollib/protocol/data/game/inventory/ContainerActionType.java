package org.geysermc.mcprotocollib.protocol.data.game.inventory;

public enum ContainerActionType {
    CLICK_ITEM {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return ClickItemAction.from(param);
        }
    },
    SHIFT_CLICK_ITEM {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return ShiftClickItemAction.from(param);
        }
    },
    MOVE_TO_HOTBAR_SLOT {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return MoveToHotbarAction.from(param);
        }
    },
    CREATIVE_GRAB_MAX_STACK {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return CreativeGrabAction.from(param);
        }
    },
    DROP_ITEM {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return DropItemAction.from(param + (slot != -999 ? 2 : 0));
        }
    },
    SPREAD_ITEM {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return SpreadItemAction.from(param);
        }
    },
    FILL_STACK {
        @Override
        public ContainerAction actionFrom(int param, int slot) {
            return FillStackAction.from(param);
        }
    };

    private static final ContainerActionType[] VALUES = values();

    public static ContainerActionType from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }

    public abstract ContainerAction actionFrom(int param, int slot);
}
