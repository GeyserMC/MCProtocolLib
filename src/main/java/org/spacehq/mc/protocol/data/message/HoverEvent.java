package org.spacehq.mc.protocol.data.message;

public class HoverEvent implements Cloneable {

    private HoverAction action;
    private Message value;

    public HoverEvent(HoverAction action, Message value) {
        this.action = action;
        this.value = value;
    }

    public HoverAction getAction() {
        return this.action;
    }

    public Message getValue() {
        return this.value;
    }

    @Override
    public HoverEvent clone() {
        return new HoverEvent(this.action, this.value.clone());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        HoverEvent that = (HoverEvent) o;

        if(action != that.action) return false;
        if(!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = action.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

}
