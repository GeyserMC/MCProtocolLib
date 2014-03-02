package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakBlockEffectData implements WorldEffectData {

	private int blockId;

	public BreakBlockEffectData(int blockId) {
		this.blockId = blockId;
	}

	public int getBlockId() {
		return this.blockId;
	}

}
