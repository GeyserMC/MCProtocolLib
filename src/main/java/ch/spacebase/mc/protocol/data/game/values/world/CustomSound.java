package ch.spacebase.mc.protocol.data.game.values.world;

public class CustomSound implements Sound {

	private String name;
	
	public CustomSound(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
