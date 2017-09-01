package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(!(o instanceof Attribute)) return false;

        Attribute that = (Attribute) o;
        return this.type == that.type &&
                this.value == that.value &&
                Objects.equals(this.modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.type, this.value, this.modifiers);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
