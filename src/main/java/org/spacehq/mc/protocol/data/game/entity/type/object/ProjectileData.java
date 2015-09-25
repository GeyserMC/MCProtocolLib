package org.spacehq.mc.protocol.data.game.entity.type.object;

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
        return this == o || (o instanceof ProjectileData && this.ownerId == ((ProjectileData) o).ownerId);
    }

    @Override
    public int hashCode() {
        return this.ownerId;
    }
}
