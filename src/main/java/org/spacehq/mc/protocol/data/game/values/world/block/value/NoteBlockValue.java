package org.spacehq.mc.protocol.data.game.values.world.block.value;

public class NoteBlockValue implements BlockValue {

	private int pitch;

	public NoteBlockValue(int pitch) {
		if(pitch < 0 || pitch > 24) {
			throw new IllegalArgumentException("Pitch must be between 0 and 24.");
		}

		this.pitch = pitch;
	}

	public int getPitch() {
		return this.pitch;
	}

}
