package com.github.steveice10.mc.protocol.data.game.chat.numbers;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.NonNull;
import net.kyori.adventure.text.format.Style;

/**
 * @param style Serialized {@link Style}
 */
public record StyledFormat(@NonNull CompoundTag style) implements NumberFormat {

}
