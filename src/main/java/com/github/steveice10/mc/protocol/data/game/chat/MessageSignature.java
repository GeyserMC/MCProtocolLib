package com.github.steveice10.mc.protocol.data.game.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class MessageSignature {
	private final int id;
	private final byte @Nullable[] messageSignature;
}
