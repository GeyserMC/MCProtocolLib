package com.github.steveice10.mc.protocol.data.message.style;

public enum ChatFormat {
    BOLD,
    UNDERLINED,
    STRIKETHROUGH,
    ITALIC,
    OBFUSCATED;

    public static ChatFormat byName(String name) {
        String lowerCase = name.toLowerCase();
        for(ChatFormat format : values()) {
            if(format.toString().equals(lowerCase)) {
                return format;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}