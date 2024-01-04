package org.geysermc.mc.protocol.data.game.level.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrialSpawnerDetectEventData implements LevelEventData {
    private final int detectedPlayers;
}
