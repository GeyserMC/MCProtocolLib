package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakBlockEffectData implements WorldEffectData {

    private int blockId;

    public BreakBlockEffectData(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockId() {
        return this.blockId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        BreakBlockEffectData that = (BreakBlockEffectData) o;

        if(blockId != that.blockId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return blockId;
    }

}
