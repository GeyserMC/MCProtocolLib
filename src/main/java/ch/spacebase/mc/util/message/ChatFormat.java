package ch.spacebase.mc.util.message;

public enum ChatFormat {

	BOLD("bold"),
	UNDERLINED("underlined"),
	STRIKETHROUGH("strikethrough"),
	ITALIC("italic"),
	OBFUSCATED("obfuscated");
	
	private String format;

	private ChatFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return this.format;
	}

}