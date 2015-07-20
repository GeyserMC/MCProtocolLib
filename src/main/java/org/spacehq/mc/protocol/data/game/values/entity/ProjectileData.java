package org.spacehq.mc.protocol.data.game.values.entity;

public class ProjectileData implements ObjectData {

    private int ownerId;

    public ProjectileData(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ProjectileData that = (ProjectileData) o;

        if(ownerId != that.ownerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ownerId;
    }

}
