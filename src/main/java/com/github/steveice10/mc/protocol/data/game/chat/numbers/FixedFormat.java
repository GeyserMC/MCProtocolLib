package com.github.steveice10.mc.protocol.data.game.chat.numbers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

@Data
@AllArgsConstructor
public class FixedFormat implements NumberFormat {

    private final @NonNull Component value;
}
