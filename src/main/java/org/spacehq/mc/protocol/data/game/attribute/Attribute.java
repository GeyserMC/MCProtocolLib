package org.spacehq.mc.protocol.data.game.attribute;

import org.spacehq.mc.protocol.data.game.values.entity.AttributeType;

import java.util.ArrayList;
import java.util.List;

public class Attribute {

    private AttributeType type;
    private double value;
    private List<AttributeModifier> modifiers;

    public Attribute(AttributeType type) {
        this(type, type.getDefault());
    }

    public Attribute(AttributeType type, double value) {
        this(type, value, new ArrayList<AttributeModifier>());
    }

    public Attribute(AttributeType type, double value, List<AttributeModifier> modifiers) {
        this.type = type;
        this.value = value;
        this.modifiers = modifiers;
    }

    public AttributeType getType() {
        return this.type;
    }

    public double getValue() {
        return this.value;
    }

    public List<AttributeModifier> getModifiers() {
        return new ArrayList<AttributeModifier>(this.modifiers);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if(Double.compare(attribute.value, value) != 0) return false;
        if(!modifiers.equals(attribute.modifiers)) return false;
        if(type != attribute.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        long temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + modifiers.hashCode();
        return result;
    }

}
