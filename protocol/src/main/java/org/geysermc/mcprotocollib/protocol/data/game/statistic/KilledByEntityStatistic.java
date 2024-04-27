package org.geysermc.mcprotocollib.protocol.data.game.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;

@Data
@AllArgsConstructor
public class KilledByEntityStatistic implements Statistic {
    private final EntityType entity;
}
