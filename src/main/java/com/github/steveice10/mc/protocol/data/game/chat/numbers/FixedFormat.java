package com.github.steveice10.mc.protocol.data.game.chat.numbers;

import lombok.NonNull;
import net.kyori.adventure.text.Component;

public record FixedFormat(@NonNull Component value) implements NumberFormat {
}
