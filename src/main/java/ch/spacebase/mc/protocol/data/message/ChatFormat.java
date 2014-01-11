package ch.spacebase.mc.protocol.data.message;

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
	
	public static ChatFormat byName(String name) {
		for(ChatFormat format : values()) {
			if(format.toString().equals(name)) {
				return format;
			}
		}
		
		return null;
	}

}