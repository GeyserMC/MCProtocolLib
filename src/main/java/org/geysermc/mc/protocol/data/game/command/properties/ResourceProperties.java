package org.geysermc.mc.protocol.data.game.command.properties;

import org.geysermc.mc.protocol.data.game.Identifier;
import lombok.Data;

@Data
public class ResourceProperties implements CommandProperties {
    private final String registryKey;

    public ResourceProperties(String registryKey) {
        this.registryKey = Identifier.formalize(registryKey);
    }
}
