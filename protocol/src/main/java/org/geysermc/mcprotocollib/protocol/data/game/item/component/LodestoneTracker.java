package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class LodestoneTracker {
    private final @Nullable GlobalPos pos;
    private final boolean tracked;
}
