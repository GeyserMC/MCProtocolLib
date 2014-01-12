package ch.spacebase.mc.protocol.data.message;

public enum ChatColor {

	WHITE,
	YELLOW,
	LIGHT_PURPLE,
	RED,
	AQUA,
	GREEN,
	BLUE,
	DARK_GRAY,
	GRAY,
	GOLD,
	DARK_PURPLE,
	DARK_RED,
	DARK_AQUA,
	DARK_GREEN,
	DARK_BLUE,
	BLACK;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

	public static ChatColor byName(String name) {
		name = name.toLowerCase();
		for(ChatColor color : values()) {
			if(color.toString().equals(name)) {
				return color;
			}
		}
		
		return null;
	}

}
