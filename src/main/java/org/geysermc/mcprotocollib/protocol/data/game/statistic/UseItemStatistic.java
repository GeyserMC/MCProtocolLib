package org.geysermc.mcprotocollib.protocol.data.game.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UseItemStatistic implements Statistic {
    private final int id;
}
