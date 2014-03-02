package org.spacehq.mc.util;

import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.CompoundTag;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NetUtil {

	private static final int[] EXPONENTS_OF_TWO = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };

	private static final int POSITION_X_SIZE = 1 + lastExponentOfTwo(nextPowerOfTwo(30000000));
	private static final int POSITION_Z_SIZE = POSITION_X_SIZE;
	private static final int POSITION_Y_SIZE = 64 - POSITION_X_SIZE - POSITION_Z_SIZE;
	private static final int POSITION_Y_SHIFT = POSITION_Z_SIZE;
	private static final int POSITION_X_SHIFT = POSITION_Y_SHIFT + POSITION_Y_SIZE;
	private static final long POSITION_X_MASK = (1L << POSITION_X_SIZE) - 1;
	private static final long POSITION_Y_MASK = (1L << POSITION_Y_SIZE) - 1;
	private static final long POSITION_Z_MASK = (1L << POSITION_Z_SIZE) - 1;

	private static int nextPowerOfTwo(int i) {
		int minusOne = i - 1;
		minusOne |= minusOne >> 1;
		minusOne |= minusOne >> 2;
		minusOne |= minusOne >> 4;
		minusOne |= minusOne >> 8;
		minusOne |= minusOne >> 16;
		return minusOne + 1;
	}

	private static boolean isPowerOfTwo(int i) {
		return i != 0 && (i & i - 1) == 0;
	}

	private static int nextExponentOfTwo(int i) {
		int power = isPowerOfTwo(i) ? i : nextPowerOfTwo(i);
		return EXPONENTS_OF_TWO[(int) (power * 125613361L >> 27) & 31];
	}

	public static int lastExponentOfTwo(int i) {
		return nextExponentOfTwo(i) - (isPowerOfTwo(i) ? 0 : 1);
	}

	public static CompoundTag readNBT(NetInput in) throws IOException {
		short length = in.readShort();
		if(length < 0) {
			return null;
		} else {
			return (CompoundTag) NBTIO.readTag(new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(in.readBytes(length)))));
		}
	}

	public static void writeNBT(NetOutput out, CompoundTag tag) throws IOException {
		if(tag == null) {
			out.writeShort(-1);
		} else {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			NBTIO.writeTag(new DataOutputStream(new GZIPOutputStream(output)), tag);
			output.close();
			byte bytes[] = output.toByteArray();
			out.writeShort((short) bytes.length);
			out.writeBytes(bytes);
		}
	}

	public static Position readPosition(NetInput in) throws IOException {
		long val = in.readLong();
		int x = (int) (val << 64 - POSITION_X_SHIFT - POSITION_X_SIZE >> 64 - POSITION_X_SIZE);
		int y = (int) (val << 64 - POSITION_Y_SHIFT - POSITION_Y_SIZE >> 64 - POSITION_Y_SIZE);
		int z = (int) (val << 64 - POSITION_Z_SIZE >> 64 - POSITION_Z_SIZE);
		return new Position(x, y, z);
	}

	public static void writePosition(NetOutput out, Position pos) throws IOException {
		out.writeLong((pos.getX() & POSITION_X_MASK) << POSITION_X_SHIFT | (pos.getY() & POSITION_Y_MASK) << POSITION_Y_SHIFT | (pos.getZ() & POSITION_Z_MASK));
	}

	public static ItemStack readItem(NetInput in) throws IOException {
		short item = in.readShort();
		if(item < 0) {
			return null;
		} else {
			return new ItemStack(item, in.readByte(), in.readShort(), readNBT(in));
		}
	}

	public static void writeItem(NetOutput out, ItemStack item) throws IOException {
		if(item == null) {
			out.writeShort(-1);
		} else {
			out.writeShort(item.getId());
			out.writeByte(item.getAmount());
			out.writeShort(item.getData());
			writeNBT(out, item.getNBT());
		}
	}

	public static EntityMetadata[] readEntityMetadata(NetInput in) throws IOException {
		List<EntityMetadata> ret = new ArrayList<EntityMetadata>();
		byte b;
		while((b = in.readByte()) != 127) {
			int typeId = (b & 224) >> 5;
			MetadataType type = MagicValues.key(MetadataType.class, typeId);
			int id = b & 31;
			Object value = null;
			switch(type) {
				case BYTE:
					value = in.readByte();
					break;
				case SHORT:
					value = in.readShort();
					break;
				case INT:
					value = in.readInt();
					break;
				case FLOAT:
					value = in.readFloat();
					break;
				case STRING:
					value = in.readString();
					break;
				case ITEM:
					value = readItem(in);
					break;
				case POSITION:
					value = new Position(in.readInt(), in.readInt(), in.readInt());
					break;
				default:
					throw new IOException("Unknown metadata type id: " + typeId);
			}

			ret.add(new EntityMetadata(id, type, value));
		}

		return ret.toArray(new EntityMetadata[ret.size()]);
	}

	public static void writeEntityMetadata(NetOutput out, EntityMetadata[] metadata) throws IOException {
		for(EntityMetadata meta : metadata) {
			int id = (MagicValues.value(Integer.class, meta.getType()) << 5 | meta.getId() & 31) & 255;
			out.writeByte(id);
			switch(meta.getType()) {
				case BYTE:
					out.writeByte((Byte) meta.getValue());
					break;
				case SHORT:
					out.writeShort((Short) meta.getValue());
					break;
				case INT:
					out.writeInt((Integer) meta.getValue());
					break;
				case FLOAT:
					out.writeFloat((Float) meta.getValue());
					break;
				case STRING:
					out.writeString((String) meta.getValue());
					break;
				case ITEM:
					writeItem(out, (ItemStack) meta.getValue());
					break;
				case POSITION:
					Position pos = (Position) meta.getValue();
					out.writeInt(pos.getX());
					out.writeInt(pos.getY());
					out.writeInt(pos.getZ());
					break;
				default:
					throw new IOException("Unmapped metadata type: " + meta.getType());
			}
		}

		out.writeByte(127);
	}

	public static ParsedChunkData dataToChunks(NetworkChunkData data) {
		Chunk chunks[] = new Chunk[16];
		int pos = 0;
		int expected = 0;
		boolean sky = false;
		// 0 = Calculate expected length and determine if the packet has skylight.
		// 1 = Create chunks from mask and get blocks.
		// 2 = Get metadata.
		// 3 = Get block light.
		// 4 = Get sky light.
		// 5 = Get extended block data.
		for(int pass = 0; pass < 5; pass++) {
			for(int ind = 0; ind < 16; ind++) {
				if((data.getMask() & 1 << ind) != 0) {
					if(pass == 0) {
						expected += 10240;
						if((data.getExtendedMask() & 1 << ind) != 0) {
							expected += 2048;
						}
					}

					if(pass == 1) {
						chunks[ind] = new Chunk(data.hasSkyLight(), (data.getExtendedMask() & 1 << ind) != 0);
						ByteArray3d blocks = chunks[ind].getBlocks();
						System.arraycopy(data.getData(), pos, blocks.getData(), 0, blocks.getData().length);
						pos += blocks.getData().length;
					}

					if(pass == 2) {
						NibbleArray3d metadata = chunks[ind].getMetadata();
						System.arraycopy(data.getData(), pos, metadata.getData(), 0, metadata.getData().length);
						pos += metadata.getData().length;
					}

					if(pass == 3) {
						NibbleArray3d blocklight = chunks[ind].getBlockLight();
						System.arraycopy(data.getData(), pos, blocklight.getData(), 0, blocklight.getData().length);
						pos += blocklight.getData().length;
					}

					if(pass == 4 && (sky || data.hasSkyLight())) {
						NibbleArray3d skylight = chunks[ind].getSkyLight();
						System.arraycopy(data.getData(), pos, skylight.getData(), 0, skylight.getData().length);
						pos += skylight.getData().length;
					}
				}

				if(pass == 5) {
					if((data.getExtendedMask() & 1 << ind) != 0) {
						if(chunks[ind] == null) {
							pos += 2048;
						} else {
							NibbleArray3d extended = chunks[ind].getExtendedBlocks();
							System.arraycopy(data.getData(), pos, extended.getData(), 0, extended.getData().length);
							pos += extended.getData().length;
						}
					}
				}
			}

			if(pass == 0) {
				if(data.getData().length >= expected) {
					sky = true;
				}
			}
		}

		byte biomeData[] = null;
		if(data.isFullChunk()) {
			biomeData = new byte[256];
			System.arraycopy(data.getData(), pos, biomeData, 0, biomeData.length);
			pos += biomeData.length;
		}

		return new ParsedChunkData(chunks, biomeData);
	}

	public static NetworkChunkData chunksToData(ParsedChunkData chunks) {
		int chunkMask = 0;
		int extendedChunkMask = 0;
		boolean fullChunk = chunks.getBiomes() != null;
		boolean sky = false;
		int length = fullChunk ? chunks.getBiomes().length : 0;
		byte[] data = null;
		int pos = 0;
		// 0 = Determine length and masks.
		// 1 = Add blocks.
		// 2 = Add metadata.
		// 3 = Add block light.
		// 4 = Add sky light.
		// 5 = Add extended block data.
		for(int pass = 0; pass < 6; pass++) {
			for(int ind = 0; ind < chunks.getChunks().length; ++ind) {
				Chunk chunk = chunks.getChunks()[ind];
				if(chunk != null && (!fullChunk || !chunk.isEmpty())) {
					if(pass == 0) {
						chunkMask |= 1 << ind;
						if(chunk.getExtendedBlocks() != null) {
							extendedChunkMask |= 1 << ind;
						}

						length += chunk.getBlocks().getData().length;
						length += chunk.getMetadata().getData().length;
						length += chunk.getBlockLight().getData().length;
						if(chunk.getSkyLight() != null) {
							length += chunk.getSkyLight().getData().length;
						}

						if(chunk.getExtendedBlocks() != null) {
							length += chunk.getExtendedBlocks().getData().length;
						}
					}

					if(pass == 1) {
						ByteArray3d blocks = chunk.getBlocks();
						System.arraycopy(blocks.getData(), 0, data, pos, blocks.getData().length);
						pos += blocks.getData().length;
					}

					if(pass == 2) {
						byte meta[] = chunk.getMetadata().getData();
						System.arraycopy(meta, 0, data, pos, meta.length);
						pos += meta.length;
					}

					if(pass == 3) {
						byte blocklight[] = chunk.getBlockLight().getData();
						System.arraycopy(blocklight, 0, data, pos, blocklight.length);
						pos += blocklight.length;
					}

					if(pass == 4 && chunk.getSkyLight() != null) {
						byte skylight[] = chunk.getSkyLight().getData();
						System.arraycopy(skylight, 0, data, pos, skylight.length);
						pos += skylight.length;
						sky = true;
					}

					if(pass == 5 && chunk.getExtendedBlocks() != null) {
						byte extended[] = chunk.getExtendedBlocks().getData();
						System.arraycopy(extended, 0, data, pos, extended.length);
						pos += extended.length;
					}
				}
			}

			if(pass == 0) {
				data = new byte[length];
			}
		}

		// Add biomes.
		if(fullChunk) {
			System.arraycopy(chunks.getBiomes(), 0, data, pos, chunks.getBiomes().length);
			pos += chunks.getBiomes().length;
		}

		return new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, sky, data);
	}

}
