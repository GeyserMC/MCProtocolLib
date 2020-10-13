package com.github.steveice10.mc.protocol.data.message.style;

public enum ClickAction {
	CHANGE_PAGE,
	COPY_TO_CLIPBOARD,
	OPEN_URL,
	RUN_COMMAND,
	SUGGEST_COMMAND;

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
