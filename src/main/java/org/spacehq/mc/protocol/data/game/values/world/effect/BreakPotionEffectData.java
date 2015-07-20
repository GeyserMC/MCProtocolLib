package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakPotionEffectData implements WorldEffectData {

    private int potionId;

    public BreakPotionEffectData(int potionId) {
        this.potionId = potionId;
    }

    public int getPotionId() {
        return this.potionId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        BreakPotionEffectData that = (BreakPotionEffectData) o;

        if(potionId != that.potionId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return potionId;
    }

}
