package org.geysermc.mcprotocollib.protocol.data.game.command.properties;

import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.Identifier;

@Data
public class ResourceProperties implements CommandProperties {
    private final String registryKey;

    public ResourceProperties(String registryKey) {
        this.registryKey = Identifier.formalize(registryKey);
    }
}
