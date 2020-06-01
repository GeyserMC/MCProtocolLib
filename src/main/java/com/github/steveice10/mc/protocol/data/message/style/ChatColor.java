package com.github.steveice10.mc.protocol.data.message.style;

public enum ChatColor {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    RESET,
    NONE;

    public static ChatColor byName(String name) {
        String lowerCase = name.toLowerCase();
        for(ChatColor color : values()) {
            if(color.toString().equals(lowerCase)) {
                return color;
            }
        }

        return ChatColor.NONE;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
