package org.geysermc.mcprotocollib.protocol.data.game.entity;

/**
 * An entity event ID that {@link EntityEventType} does not know.
 * The vanilla client ignores these events.
 */
public record UnknownEntityEvent(int id) implements EntityEvent {
    @Override
    public int getId() {
        return id;
    }
}
