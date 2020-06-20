package com.github.steveice10.mc.protocol.data.message.style;

public enum ClickAction {
    RUN_COMMAND,
    SUGGEST_COMMAND,
    OPEN_URL,
    CHANGE_PAGE;

    public static ClickAction byName(String name) {
        String lowerCase = name.toLowerCase();
        for(ClickAction action : values()) {
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
