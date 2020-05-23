package com.github.steveice10.mc.protocol.data;

import lombok.Getter;

@Getter
public class UnmappedKeyException extends IllegalArgumentException {
    private Enum<?> key;
    private Class<?> valueType;

    public UnmappedKeyException(Object key, Class<?> valueType) {
        super("Key " + key + " has no mapping for value class " + valueType.getName() + ".");
    }
}
