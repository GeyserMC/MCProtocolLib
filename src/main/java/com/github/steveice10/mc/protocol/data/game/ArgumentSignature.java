package com.github.steveice10.mc.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArgumentSignature {
    private final String name;
    private final byte[] signature;
}
