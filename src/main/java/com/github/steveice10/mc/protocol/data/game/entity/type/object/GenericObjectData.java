package com.github.steveice10.mc.protocol.data.game.entity.type.object;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class GenericObjectData implements ObjectData {
    private int value;

    public GenericObjectData(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof GenericObjectData)) return false;

        GenericObjectData that = (GenericObjectData) o;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.value);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
