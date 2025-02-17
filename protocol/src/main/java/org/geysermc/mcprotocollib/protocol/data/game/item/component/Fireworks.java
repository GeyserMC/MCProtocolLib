package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.List;

@Data
@With
@Builder(toBuilder = true)
public class Fireworks {
    private final int flightDuration;
    private final List<FireworkExplosion> explosions;

    public Fireworks(int flightDuration, List<FireworkExplosion> explosions) {
        this.flightDuration = flightDuration;
        this.explosions = List.copyOf(explosions);
    }

    @Data
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class FireworkExplosion {
        private final int shapeId;
        private final int[] colors;
        private final int[] fadeColors;
        private final boolean hasTrail;
        private final boolean hasTwinkle;
    }
}
