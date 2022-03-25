package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.mc.protocol.data.game.level.vibration.VibrationSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VibrationParticleData implements ParticleData {
	private final VibrationSource destination;
	private final int arrivalInTicks;
}
