package ch.spacebase.mc.util.message;

public enum ChatColor {

	WHITE("white"),
	YELLOW("yellow"),
	LIGHT_PURPLE("light_purple"),
	RED("red"),
	AQUA("aqua"),
	GREEN("green"),
	BLUE("blue"),
	DARK_GRAY("dark_gray"),
	GRAY("gray"),
	GOLD("gold"),
	DARK_PURPLE("dark_purple"),
	DARK_RED("dark_red"),
	DARK_AQUA("dark_aqua"),
	DARK_GREEN("dark_green"),
	DARK_BLUE("dark_blue"),
	BLACK("black");
	
	private String color;

	private ChatColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return this.color;
	}

	public static ChatColor byValue(String val) {
		for(ChatColor color : values()) {
			if(color.toString().equals(val)) {
				return color;
			}
		}
		
		return null;
	}

}
