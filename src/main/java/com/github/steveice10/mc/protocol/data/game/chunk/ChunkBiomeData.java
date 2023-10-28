package com.github.steveice10.mc.protocol.data.game.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;

public record ChunkBiomeData(int x, int z, byte[] buffer) {
}
