package org.spacehq.mc.protocol.data.game.world.block.value;

public class ChestValue implements BlockValue {
    private int viewers;

    public ChestValue(int viewers) {
        this.viewers = viewers;
    }

    public int getViewers() {
        return this.viewers;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ChestValue && this.viewers == ((ChestValue) o).viewers;
    }

    @Override
    public int hashCode() {
        return this.viewers;
    }
}
