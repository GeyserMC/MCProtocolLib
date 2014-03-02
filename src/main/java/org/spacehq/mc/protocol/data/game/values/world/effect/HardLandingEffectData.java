package org.spacehq.mc.protocol.data.game.values.world.effect;

public class HardLandingEffectData implements WorldEffectData {

	private int damagingDistance;

	public HardLandingEffectData(int damagingDistance) {
		this.damagingDistance = damagingDistance;
	}

	public int getDamagingDistance() {
		return this.damagingDistance;
	}

}
