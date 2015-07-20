package org.spacehq.mc.protocol.data.game.values.world.block.value;

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
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        GenericBlockValue that = (GenericBlockValue) o;

        if(value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

}
