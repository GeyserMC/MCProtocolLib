package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class EntityMetadata {
    private final int id;
    private final @NonNull MetadataType type;
    private final Object value;
}
