package org.spacehq.mc.protocol.data.game.values.entity;

public class SplashPotionData implements ObjectData {

    private int potionData;

    public SplashPotionData(int potionData) {
        this.potionData = potionData;
    }

    public int getPotionData() {
        return this.potionData;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        SplashPotionData that = (SplashPotionData) o;

        if(potionData != that.potionData) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return potionData;
    }

}
