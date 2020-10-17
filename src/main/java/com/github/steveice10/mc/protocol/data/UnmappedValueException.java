package com.github.steveice10.mc.protocol.data;

import lombok.Getter;

@Getter
public class UnmappedValueException extends IllegalArgumentException {
    private Object value;
    private Class<?> keyType;

    public UnmappedValueException(Object value, Class<?> keyType) {
        super("Value " + value + " has no mapping for key class " + keyType.getName() + ".");
    }
}
