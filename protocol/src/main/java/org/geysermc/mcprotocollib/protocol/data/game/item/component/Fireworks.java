package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Fireworks {
    private final int flightDuration;
    private final List<FireworkExplosion> explosions;

    @Data
    @AllArgsConstructor
    public static class FireworkExplosion {
        private final int shapeId;
        private final int[] colors;
        private final int[] fadeColors;
        private final boolean hasTrail;
        private final boolean hasTwinkle;
    }
}
