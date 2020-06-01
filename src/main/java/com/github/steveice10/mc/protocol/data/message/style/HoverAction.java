package com.github.steveice10.mc.protocol.data.message.style;

public enum HoverAction {
    SHOW_TEXT,
    SHOW_ITEM,
    SHOW_ENTITY;

    public static HoverAction byName(String name) {
        String lowerCase = name.toLowerCase();
        for(HoverAction action : values()) {
            if(action.toString().equals(lowerCase)) {
                return action;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
