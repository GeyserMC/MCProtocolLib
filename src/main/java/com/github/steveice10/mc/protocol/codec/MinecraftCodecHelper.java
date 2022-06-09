package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.data.game.chunk.BitStorage;
import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.DataPalette;
import com.github.steveice10.mc.protocol.data.game.chunk.NibbleArray3d;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.*;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.data.game.entity.EntityEvent;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEvent;
import com.github.steveice10.mc.protocol.data.game.level.particle.*;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.BlockPositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.EntityPositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSourceType;
import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.statistic.StatisticCategory;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.github.steveice10.packetlib.codec.BasePacketCodecHelper;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

@RequiredArgsConstructor
public class MinecraftCodecHelper extends BasePacketCodecHelper {
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 12;
    private static final int POSITION_Z_SIZE = 38;
    private static final int POSITION_Y_SHIFT = 0xFFF;
    private static final int POSITION_WRITE_SHIFT = 0x3FFFFFF;

    private final Int2ObjectMap<LevelEvent> levelEvents;
    private final Map<String, BuiltinSound> soundNames;

    protected CompoundTag registry;

    public UUID readUUID(ByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public void writeUUID(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public byte[] readByteArray(ByteBuf buf) {
        return this.readByteArray(buf, this::readVarInt);
    }

    public byte[] readByteArray(ByteBuf buf, ToIntFunction<ByteBuf> reader) {
        int length = reader.applyAsInt(buf);
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    public void writeByteArray(ByteBuf buf, byte[] bytes) {
        this.writeByteArray(buf, bytes, this::writeVarInt);
    }

    public void writeByteArray(ByteBuf buf, byte[] bytes, ObjIntConsumer<ByteBuf> writer) {
        writer.accept(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    public long[] readLongArray(ByteBuf buf) {
        return this.readLongArray(buf, this::readVarInt);
    }

    public long[] readLongArray(ByteBuf buf, ToIntFunction<ByteBuf> reader) {
        int length = reader.applyAsInt(buf);
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }

        long[] l = new long[length];
        for (int index = 0; index < length; index++) {
            l[index] = buf.readLong();
        }

        return l;
    }

    public void writeLongArray(ByteBuf buf, long[] l) {
        this.writeLongArray(buf, l, this::writeVarInt);
    }

    public void writeLongArray(ByteBuf buf, long[] l, ObjIntConsumer<ByteBuf> writer) {
        writer.accept(buf, l.length);
        for (long value : l) {
            buf.writeLong(value);
        }
    }

    public CompoundTag readTag(ByteBuf buf) throws IOException {
        return readTag(buf, CompoundTag.class);
    }

    @Nullable
    public <T extends Tag> T readTag(ByteBuf buf, Class<T> expected) throws IOException {
        Tag tag = NBTIO.readTag(new InputStream() {
            @Override
            public int read() {
                return buf.readUnsignedByte();
            }
        });

        if (tag == null) {
            return null;
        }

        if (tag.getClass() != expected) {
            throw new IllegalArgumentException("Expected tag of type " + expected.getName() + " but got " + tag.getClass().getName());
        }

        return expected.cast(tag);
    }

    public CompoundTag readTagLE(ByteBuf buf) throws IOException {
        return readTagLE(buf, CompoundTag.class);
    }

    @Nullable
    public <T extends Tag> T readTagLE(ByteBuf buf, Class<T> expected) throws IOException {
        Tag tag = NBTIO.readTag(new InputStream() {
            @Override
            public int read() {
                return buf.readUnsignedByte();
            }
        }, true);

        if (tag == null) {
            return null;
        }

        if (tag.getClass() != expected) {
            throw new IllegalArgumentException("Expected tag of type " + expected.getName() + " but got " + tag.getClass().getName());
        }

        return expected.cast(tag);
    }

    public <T extends Tag> void writeTag(ByteBuf buf, T tag) throws IOException {
        NBTIO.writeTag(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                buf.writeByte(b);
            }
        }, tag);
    }

    public <T extends Tag> void writeTagLE(ByteBuf buf, T tag) throws IOException {
        NBTIO.writeTag(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                buf.writeByte(b);
            }
        }, tag, true);
    }

    public ItemStack readItemStack(ByteBuf buf) throws IOException {
        boolean present = buf.readBoolean();
        if (!present) {
            return null;
        }

        int item = this.readVarInt(buf);
        return new ItemStack(item, buf.readByte(), this.readTag(buf));
    }

    public void writeItemStack(ByteBuf buf, ItemStack item) throws IOException {
        buf.writeBoolean(item != null);
        if (item != null) {
            this.writeVarInt(buf, item.getId());
            buf.writeByte(item.getAmount());
            this.writeTag(buf, item.getNbt());
        }
    }

    public Vector3i readPosition(ByteBuf buf) {
        long val = buf.readLong();

        int x = (int) (val >> POSITION_X_SIZE);
        int y = (int) (val << 52 >> 52);
        int z = (int) (val << 26 >> POSITION_Z_SIZE);

        return Vector3i.from(x, y, z);
    }

    public void writePosition(ByteBuf buf, Vector3i pos) {
        long x = pos.getX() & POSITION_WRITE_SHIFT;
        long y = pos.getY() & POSITION_Y_SHIFT;
        long z = pos.getZ() & POSITION_WRITE_SHIFT;

        buf.writeLong(x << POSITION_X_SIZE | z << POSITION_Y_SIZE | y);
    }

    public Vector3f readRotation(ByteBuf buf) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();

        return Vector3f.from(x, y, z);
    }

    public void writeRotation(ByteBuf buf, Vector3f rot) {
        buf.writeFloat(rot.getX());
        buf.writeFloat(rot.getY());
        buf.writeFloat(rot.getZ());
    }

    public Direction readDirection(ByteBuf buf) {
        return Direction.from(this.readVarInt(buf));
    }

    public void writeDirection(ByteBuf buf, Direction dir) {
        this.writeEnum(buf, dir);
    }

    public Pose readPose(ByteBuf buf) {
        return Pose.from(this.readVarInt(buf));
    }

    public void writePose(ByteBuf buf, Pose pose) {
        this.writeEnum(buf, pose);
    }

    public PaintingType readPaintingType(ByteBuf buf) {
        return PaintingType.from(this.readVarInt(buf));
    }

    public void writePaintingType(ByteBuf buf, PaintingType type) {
        this.writeEnum(buf, type);
    }

    private void writeEnum(ByteBuf buf, Enum<?> e){
        this.writeVarInt(buf, e.ordinal());
    }

    public Component readComponent(ByteBuf buf) {
        return DefaultComponentSerializer.get().deserialize(this.readString(buf));
    }

    public void writeComponent(ByteBuf buf, Component component) {
        this.writeString(buf, DefaultComponentSerializer.get().serialize(component));
    }

    public EntityMetadata<?, ?>[] readEntityMetadata(ByteBuf buf) throws IOException {
        List<EntityMetadata<?, ?>> ret = new ArrayList<>();
        int id;
        while ((id = buf.readUnsignedByte()) != 255) {
            ret.add(this.readMetadata(buf, id));
        }

        return ret.toArray(new EntityMetadata[0]);
    }

    public void writeEntityMetadata(ByteBuf buf, EntityMetadata<?, ?>[] metadata) throws IOException {
        for (EntityMetadata<?, ?> meta : metadata) {
            this.writeMetadata(buf, meta);
        }

        buf.writeByte(255);
    }

    public EntityMetadata<?, ?> readMetadata(ByteBuf buf, int id) throws IOException {
        MetadataType<?> type = this.readMetadataType(buf);
        return type.readMetadata(this, buf, id);
    }

    public void writeMetadata(ByteBuf buf, EntityMetadata<?, ?> metadata) throws IOException {
        buf.writeByte(metadata.getId());
        this.writeMetadataType(buf, metadata.getType());
        metadata.write(this, buf);
    }

    public MetadataType<?> readMetadataType(ByteBuf buf) {
        int id = this.readVarInt(buf);
        if (id >= MetadataType.size()) {
            throw new IllegalArgumentException("Received id " + id + " for MetadataType when the maximum was " + MetadataType.size() + "!");
        }

        return MetadataType.from(id);
    }

    public void writeMetadataType(ByteBuf buf, MetadataType<?> type) {
        this.writeVarInt(buf, type.getId());
    }

    public GlobalPos readGlobalPos(ByteBuf buf) {
        String dimension = Identifier.formalize(this.readString(buf));
        Vector3i pos = this.readPosition(buf);
        return new GlobalPos(dimension, pos);
    }

    public void writeGlobalPos(ByteBuf buf, GlobalPos pos) {
        this.writeString(buf, pos.getDimension());
        this.writePosition(buf, pos.getPosition());
    }

    public ParticleType readParticleType(ByteBuf buf) {
        return ParticleType.from(this.readVarInt(buf));
    }

    public void writeParticleType(ByteBuf buf, ParticleType type) {
        this.writeEnum(buf, type);
    }

    public Particle readParticle(ByteBuf buf) throws IOException {
        ParticleType particleType = this.readParticleType(buf);
        return new Particle(particleType, this.readParticleData(buf, particleType));
    }

    public void writeParticle(ByteBuf buf, Particle particle) throws IOException {
        this.writeEnum(buf, particle.getType());
        this.writeParticleData(buf, particle.getType(), particle.getData());
    }

    public ParticleData readParticleData(ByteBuf buf, ParticleType type) throws IOException {
        switch (type) {
            case BLOCK:
            case BLOCK_MARKER:
                return new BlockParticleData(this.readVarInt(buf));
            case DUST:
                float red = buf.readFloat();
                float green = buf.readFloat();
                float blue = buf.readFloat();
                float scale = buf.readFloat();
                return new DustParticleData(red, green, blue, scale);
            case DUST_COLOR_TRANSITION:
                red = buf.readFloat();
                green = buf.readFloat();
                blue = buf.readFloat();
                scale = buf.readFloat();
                float newRed = buf.readFloat();
                float newGreen = buf.readFloat();
                float newBlue = buf.readFloat();
                return new DustColorTransitionParticleData(red, green, blue, scale, newRed, newGreen, newBlue);
            case FALLING_DUST:
                return new FallingDustParticleData(this.readVarInt(buf));
            case ITEM:
                return new ItemParticleData(this.readItemStack(buf));
            case SCULK_CHARGE:
                return new SculkChargeParticleData(buf.readFloat());
            case SHRIEK:
                return new ShriekParticleData(this.readVarInt(buf));
            case VIBRATION:
                return new VibrationParticleData(this.readPositionSource(buf), this.readVarInt(buf));
            default:
                return null;
        }
    }

    public void writeParticleData(ByteBuf buf, ParticleType type, ParticleData data) throws IOException {
        switch (type) {
            case BLOCK:
            case BLOCK_MARKER:
                this.writeVarInt(buf, ((BlockParticleData) data).getBlockState());
                break;
            case DUST:
                buf.writeFloat(((DustParticleData) data).getRed());
                buf.writeFloat(((DustParticleData) data).getGreen());
                buf.writeFloat(((DustParticleData) data).getBlue());
                buf.writeFloat(((DustParticleData) data).getScale());
                break;
            case DUST_COLOR_TRANSITION:
                buf.writeFloat(((DustParticleData) data).getRed());
                buf.writeFloat(((DustParticleData) data).getGreen());
                buf.writeFloat(((DustParticleData) data).getBlue());
                buf.writeFloat(((DustParticleData) data).getScale());
                buf.writeFloat(((DustColorTransitionParticleData) data).getNewRed());
                buf.writeFloat(((DustColorTransitionParticleData) data).getNewGreen());
                buf.writeFloat(((DustColorTransitionParticleData) data).getNewBlue());
                break;
            case FALLING_DUST:
                this.writeVarInt(buf, ((FallingDustParticleData) data).getBlockState());
                break;
            case ITEM:
                this.writeItemStack(buf, ((ItemParticleData) data).getItemStack());
                break;
            case SCULK_CHARGE:
                buf.writeFloat(((SculkChargeParticleData) data).getRoll());
                break;
            case SHRIEK:
                this.writeVarInt(buf, ((ShriekParticleData) data).getDelay());
                break;
            case VIBRATION:
                this.writePositionSource(buf, ((VibrationParticleData) data).getPositionSource());
                this.writeVarInt(buf, ((VibrationParticleData) data).getArrivalTicks());
                break;
        }
    }

    public PositionSource readPositionSource(ByteBuf buf) {
        PositionSourceType type = this.readPositionSourceType(buf);
        switch (type) {
            case BLOCK:
                return new BlockPositionSource(this.readPosition(buf));
            case ENTITY:
                return new EntityPositionSource(this.readVarInt(buf), buf.readFloat());
            default:
                throw new IllegalStateException("Unknown position source type!");
        }
    }

    public void writePositionSource(ByteBuf buf, PositionSource positionSource) {
        this.writePositionSourceType(buf, positionSource.getType());
        if (positionSource instanceof BlockPositionSource) {
            this.writePosition(buf, ((BlockPositionSource) positionSource).getPosition());
        } else if (positionSource instanceof EntityPositionSource) {
            this.writeVarInt(buf, ((EntityPositionSource) positionSource).getEntityId());
            buf.writeFloat(((EntityPositionSource) positionSource).getYOffset());
        }

        throw new IllegalStateException("Unknown position source type!");
    }

    public PositionSourceType readPositionSourceType(ByteBuf buf) {
        return MagicValues.key(PositionSourceType.class, Identifier.formalize(this.readString(buf)));
    }

    public void writePositionSourceType(ByteBuf buf, PositionSourceType type) {
        this.writeString(buf, MagicValues.value(String.class, type));
    }

    public VillagerData readVillagerData(ByteBuf buf) {
        return new VillagerData(this.readVarInt(buf), this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeVillagerData(ByteBuf buf, VillagerData villagerData) {
        this.writeVarInt(buf, villagerData.getType());
        this.writeVarInt(buf, villagerData.getProfession());
        this.writeVarInt(buf, villagerData.getLevel());
    }

    public ModifierOperation readModifierOperation(ByteBuf buf) {
        return ModifierOperation.from(buf.readByte());
    }

    public void writeModifierOperation(ByteBuf buf, ModifierOperation operation) {
        buf.writeByte(operation.ordinal());
    }

    public Effect readEffect(ByteBuf buf) {
        return Effect.from(this.readVarInt(buf) - 1);
    }

    public void writeEffect(ByteBuf buf, Effect effect) {
        this.writeVarInt(buf, effect.ordinal() + 1);
    }

    public BlockBreakStage readBlockBreakStage(ByteBuf buf) {
        int stage = buf.readUnsignedByte();
        if (stage >= 0 && stage < 10) {
            return BlockBreakStage.STAGES[stage];
        } else {
            return BlockBreakStage.RESET;
        }
    }

    public void writeBlockBreakStage(ByteBuf buf, BlockBreakStage stage) {
        if (stage == BlockBreakStage.RESET) {
            buf.writeByte(255);
        } else {
            buf.writeByte(stage.ordinal());
        }
    }

    public BlockEntityType readBlockEntityType(ByteBuf buf) {
        return BlockEntityType.from(this.readVarInt(buf));
    }

    public void writeBlockEntityType(ByteBuf buf, BlockEntityType type) {
        this.writeEnum(buf, type);
    }

    public LightUpdateData readLightUpdateData(ByteBuf buf) {
        boolean trustEdges = buf.readBoolean();

        BitSet skyYMask = BitSet.valueOf(this.readLongArray(buf));
        BitSet blockYMask = BitSet.valueOf(this.readLongArray(buf));
        BitSet emptySkyYMask = BitSet.valueOf(this.readLongArray(buf));
        BitSet emptyBlockYMask = BitSet.valueOf(this.readLongArray(buf));

        int skyUpdateSize = this.readVarInt(buf);
        List<byte[]> skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(this.readByteArray(buf));
        }

        int blockUpdateSize = this.readVarInt(buf);
        List<byte[]> blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(this.readByteArray(buf));
        }

        return new LightUpdateData(skyYMask, blockYMask, emptySkyYMask, emptyBlockYMask, skyUpdates, blockUpdates, trustEdges);
    }

    public void writeLightUpdateData(ByteBuf buf, LightUpdateData data) {
        buf.writeBoolean(data.isTrustEdges());

        writeBitSet(buf, data.getSkyYMask());
        writeBitSet(buf, data.getBlockYMask());
        writeBitSet(buf, data.getEmptySkyYMask());
        writeBitSet(buf, data.getEmptyBlockYMask());

        this.writeVarInt(buf, data.getSkyUpdates().size());
        for (byte[] array : data.getSkyUpdates()) {
            this.writeByteArray(buf, array);
        }

        this.writeVarInt(buf, data.getBlockUpdates().size());
        for (byte[] array : data.getBlockUpdates()) {
            this.writeByteArray(buf, array);
        }
    }

    private void writeBitSet(ByteBuf buf, BitSet bitSet) {
        long[] array = bitSet.toLongArray();
        this.writeLongArray(buf, array);
    }

    public LevelEvent readLevelEvent(ByteBuf buf) {
        return this.levelEvents.get(buf.readInt());
    }

    public void writeLevelEvent(ByteBuf buf, LevelEvent event) {
        buf.writeInt(event.getId());
    }

    public StatisticCategory readStatisticCategory(ByteBuf buf) {
        return StatisticCategory.from(this.readVarInt(buf));
    }

    public void writeStatisticCategory(ByteBuf buf, StatisticCategory category) {
        this.writeEnum(buf, category);
    }

    public SoundCategory readSoundCategory(ByteBuf buf) {
        return SoundCategory.from(this.readVarInt(buf));
    }

    public void writeSoundCategory(ByteBuf buf, SoundCategory category) {
        this.writeEnum(buf, category);
    }

    public BuiltinSound readBuiltinSound(ByteBuf buf) {
        return BuiltinSound.from(this.readVarInt(buf));
    }

    public void writeBuiltinSound(ByteBuf buf, BuiltinSound sound) {
        this.writeEnum(buf, sound);
    }

    @Nullable
    public BuiltinSound getBuiltinSound(String name) {
        return this.soundNames.get(name);
    }

    public EntityEvent readEntityEvent(ByteBuf buf) {
        return EntityEvent.from(buf.readByte());
    }

    public void writeEntityEvent(ByteBuf buf, EntityEvent event) {
        buf.writeByte(event.ordinal());
    }

    public Ingredient readRecipeIngredient(ByteBuf buf) throws IOException {
        ItemStack[] options = new ItemStack[this.readVarInt(buf)];
        for (int i = 0; i < options.length; i++) {
            options[i] = this.readItemStack(buf);
        }

        return new Ingredient(options);
    }

    public void writeRecipeIngredient(ByteBuf buf, Ingredient ingredient) throws IOException {
        this.writeVarInt(buf, ingredient.getOptions().length);
        for (ItemStack option : ingredient.getOptions()) {
            this.writeItemStack(buf, option);
        }
    }

    public DataPalette readDataPalette(ByteBuf buf, PaletteType paletteType, int globalPaletteBits) throws IOException {
        int bitsPerEntry = buf.readByte();
        Palette palette = this.readPalette(buf, paletteType, bitsPerEntry);
        BitStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), this.readLongArray(buf));
        } else {
            this.readVarInt(buf);
            storage = null;
        }

        return new DataPalette(palette, storage, paletteType, globalPaletteBits);
    }

    public void writeDataPalette(ByteBuf buf, DataPalette palette) {
        if (palette.getPalette() instanceof SingletonPalette) {
            buf.writeByte(0); // Bits per entry
            this.writeVarInt(buf, palette.getPalette().idToState(0));
            this.writeVarInt(buf, 0); // Data length
            return;
        }

        buf.writeByte(palette.getStorage().getBitsPerEntry());

        if (!(palette.getPalette() instanceof GlobalPalette)) {
            int paletteLength = palette.getPalette().size();
            this.writeVarInt(buf, paletteLength);
            for (int i = 0; i < paletteLength; i++) {
                this.writeVarInt(buf, palette.getPalette().idToState(i));
            }
        }

        long[] data = palette.getStorage().getData();
        this.writeLongArray(buf, data);
    }

    private Palette readPalette(ByteBuf buf, PaletteType paletteType, int bitsPerEntry) throws IOException {
        if (bitsPerEntry > paletteType.getMaxBitsPerEntry()) {
            return new GlobalPalette();
        }

        if (bitsPerEntry == 0) {
            return new SingletonPalette(this.readVarInt(buf));
        }

        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry, buf, this);
        } else {
            return new MapPalette(bitsPerEntry, buf, this);
        }
    }

    public ChunkSection readChunkSection(ByteBuf buf, int globalBiomePaletteBits) throws IOException {
        int blockCount = buf.readShort();

        DataPalette chunkPalette = this.readDataPalette(buf, PaletteType.CHUNK, DataPalette.GLOBAL_PALETTE_BITS_PER_ENTRY);
        DataPalette biomePalette = this.readDataPalette(buf, PaletteType.BIOME, globalBiomePaletteBits);
        return new ChunkSection(blockCount, chunkPalette, biomePalette);
    }

    public void writeChunkSection(ByteBuf buf, ChunkSection section) {
        buf.writeShort(section.getBlockCount());
        this.writeDataPalette(buf, section.getChunkData());
        this.writeDataPalette(buf, section.getBiomeData());
    }

    public NibbleArray3d readNibbleArray(ByteBuf buf, int size) {
        return new NibbleArray3d(this.readByteArray(buf, ignored -> size));
    }

    public void writeNibbleArray(ByteBuf buf, NibbleArray3d nibbleArray) {
        buf.writeBytes(nibbleArray.getData());
    }

    /**
     * The game registry sent to clients from the {@link ClientboundLoginPacket}.
     * Implementations are required to set this value if they intend to use it.
     *
     * @return the game registry
     */
    @Nullable
    public CompoundTag getRegistry() {
        return this.registry;
    }

    public void setRegistry(CompoundTag registry) {
        this.registry = registry;
    }
}
