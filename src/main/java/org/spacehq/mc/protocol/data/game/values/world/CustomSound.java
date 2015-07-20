package org.spacehq.mc.protocol.data.game.values.world;

public class CustomSound implements Sound {

    private String name;

    public CustomSound(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        CustomSound that = (CustomSound) o;

        if(!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
