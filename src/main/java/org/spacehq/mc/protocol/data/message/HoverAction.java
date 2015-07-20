package org.spacehq.mc.protocol.data.message;

public enum HoverAction {

    SHOW_TEXT,
    SHOW_ITEM,
    SHOW_ACHIEVEMENT,
    SHOW_ENTITY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static HoverAction byName(String name) {
        name = name.toLowerCase();
        for(HoverAction action : values()) {
            if(action.toString().equals(name)) {
                return action;
            }
        }

        return null;
    }

}
