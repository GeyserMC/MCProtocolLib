package org.geysermc.mcprotocollib.protocol.data.game.command.properties;

import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

@Data
public class ResourceProperties implements CommandProperties {
    private final ResourceLocation registryKey;

    public ResourceProperties(String registryKey) {
        this.registryKey = ResourceLocation.fromString(registryKey);
    }
}
