package org.geysermc.mcprotocollib.protocol.data.game.chat.numbers;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

@Data
@AllArgsConstructor
public class FixedFormat implements NumberFormat {

    private final Component value;
}
