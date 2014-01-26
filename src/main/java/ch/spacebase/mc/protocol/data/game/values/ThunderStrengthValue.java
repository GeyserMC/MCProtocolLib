package ch.spacebase.mc.protocol.data.game.values;

public class ThunderStrengthValue implements ClientNotificationValue {

	private float strength;
	
	public ThunderStrengthValue(float strength) {
		if(strength > 1) {
			strength = 1;
		}
		
		if(strength < 0) {
			strength = 0;
		}
		
		this.strength = strength;
	}
	
	public float getStrength() {
		return this.strength;
	}
	
}
