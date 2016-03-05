package org.spacehq.mc.protocol.data.game.world.effect;

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
        return o instanceof BreakPotionEffectData && this.potionId == ((BreakPotionEffectData) o).potionId;
    }

    @Override
    public int hashCode() {
        return this.potionId;
    }
}
