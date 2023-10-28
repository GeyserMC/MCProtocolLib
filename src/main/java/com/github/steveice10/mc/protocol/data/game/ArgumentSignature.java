package com.github.steveice10.mc.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;

public record ArgumentSignature(String name, byte[] signature) {
}
