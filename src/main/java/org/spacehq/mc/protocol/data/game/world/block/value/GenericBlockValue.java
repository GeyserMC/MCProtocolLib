package org.spacehq.mc.protocol.data.game.world.block.value;

public class GenericBlockValue implements BlockValue {
    private int value;

    public GenericBlockValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GenericBlockValue && this.value == ((GenericBlockValue) o).value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }
}
