package com.github.steveice10.mc.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LastSeenMessage {
	private final UUID profileId;
	private final byte[] lastSignature;
}
