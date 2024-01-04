package org.geysermc.mc.protocol.data.game.statistic;

import org.geysermc.mc.protocol.data.game.entity.type.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KillEntityStatistic implements Statistic {
    private final EntityType entity;
}
