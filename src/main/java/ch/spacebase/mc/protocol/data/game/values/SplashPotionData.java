package ch.spacebase.mc.protocol.data.game.values;

public class SplashPotionData implements ObjectData{

	private int potionData;
	
	public SplashPotionData(int potionData) {
		this.potionData = potionData;
	}
	
	public int getPotionData() {
		return this.potionData;
	}
	
}
