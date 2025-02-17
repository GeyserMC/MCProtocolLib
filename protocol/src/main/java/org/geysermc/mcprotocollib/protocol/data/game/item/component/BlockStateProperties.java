package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Data;

import java.util.Map;

@Data
public class BlockStateProperties {
    private final Map<String, String> properties;

    public BlockStateProperties(Map<String, String> properties) {
        this.properties = Map.copyOf(properties);
    }
}
