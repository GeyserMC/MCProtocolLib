package ch.spacebase.mc.auth.request;

public class Agent {

	private String name;
	private int version;

	public Agent(String name, int version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return this.name;
	}

	public int getVersion() {
		return this.version;
	}

}
