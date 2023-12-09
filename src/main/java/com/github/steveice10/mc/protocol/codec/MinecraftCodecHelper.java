package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.CheckedBiConsumer;
import com.github.steveice10.mc.protocol.CheckedFunction;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.chat.numbers.BlankFormat;
import com.github.steveice10.mc.protocol.data.game.chat.numbers.FixedFormat;
import com.github.steveice10.mc.protocol.data.game.chat.numbers.NumberFormat;
import com.github.steveice10.mc.protocol.data.game.chat.numbers.StyledFormat;
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
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerSpawnInfo;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEvent;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEventType;
import com.github.steveice10.mc.protocol.data.game.level.event.UnknownLevelEvent;
import com.github.steveice10.mc.protocol.data.game.level.particle.*;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.BlockPositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.EntityPositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSource;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSourceType;
import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.statistic.StatisticCategory;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.github.steveice10.packetlib.codec.BasePacketCodecHelper;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

@RequiredArgsConstructor
public class MinecraftCodecHelper extends BasePacketCodecHelper {
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 12;
    private static final int POSITION_Z_SIZE = 38;
    private static final int POSITION_Y_SHIFT = 0xFFF;
    private static final int POSITION_WRITE_SHIFT = 0x3FFFFFF;

    private final Int2ObjectMap<LevelEventType> levelEvents;
    private final Map<String, BuiltinSound> soundNames;

    @Nullable
    public <T, E extends Throwable> T readNullable(ByteBuf buf, CheckedFunction<ByteBuf, T, E> ifPresent) throws E {
        if (buf.readBoolean()) {
            return ifPresent.apply(buf);
        } else {
            return null;
        }
    }

    public <T, E extends Throwable> void writeNullable(ByteBuf buf, @Nullable T value, CheckedBiConsumer<ByteBuf, T, E> ifPresent) throws E {
        if (value != null) {
            buf.writeBoolean(true);
            ifPresent.accept(buf, value);
        } else {
            buf.writeBoolean(false);
        }
    }

    public String readResourceLocation(ByteBuf buf) {
        return Identifier.formalize(this.readString(buf));
    }

    public void writeResourceLocation(ByteBuf buf, String location) {
        this.writeString(buf, location);
    }

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

    @Nullable
    public CompoundTag readAnyTag(ByteBuf buf) {
        return readAnyTag(buf, CompoundTag.class);
    }

    @NotNull
    public CompoundTag readAnyTagOrThrow(ByteBuf buf) {
        CompoundTag tag = readAnyTag(buf);
        if (tag == null) {
            throw new IllegalArgumentException("Got end-tag when trying to read CompoundTag");
        }

        return tag;
    }

    @Nullable
    public <T extends Tag> T readAnyTag(ByteBuf buf, Class<T> expected) {
        Tag tag;
        try {
            tag = NBTIO.readAnyTag(new InputStream() {
                @Override
                public int read() {
                    return buf.readUnsignedByte();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (tag == null) {
            return null;
        }

        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException("Expected tag of type " + expected.getName() + " but got " + tag.getClass().getName());
        }

        return expected.cast(tag);
    }

    public <T extends Tag> void writeAnyTag(ByteBuf buf, @Nullable T tag) {
        try {
            NBTIO.writeAnyTag(new OutputStream() {
                @Override
                public void write(int b) {
                    buf.writeByte(b);
                }
            }, tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public ItemStack readItemStack(ByteBuf buf) {
        boolean present = buf.readBoolean();
        if (!present) {
            return null;
        }

        int item = this.readVarInt(buf);
        return new ItemStack(item, buf.readByte(), this.readAnyTag(buf));
    }

    public void writeItemStack(ByteBuf buf, ItemStack item) {
        buf.writeBoolean(item != null);
        if (item != null) {
            this.writeVarInt(buf, item.getId());
            buf.writeByte(item.getAmount());
            this.writeAnyTag(buf, item.getNbt());
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

    public Vector4f readQuaternion(ByteBuf buf) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();
        float w = buf.readFloat();

        return Vector4f.from(x, y, z, w);
    }

    public void writeQuaternion(ByteBuf buf, Vector4f vec4) {
        buf.writeFloat(vec4.getX());
        buf.writeFloat(vec4.getY());
        buf.writeFloat(vec4.getZ());
        buf.writeFloat(vec4.getW());
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

    public SnifferState readSnifferState(ByteBuf buf) {
        return SnifferState.from(this.readVarInt(buf));
    }

    public void writeSnifferState(ByteBuf buf, SnifferState state) {
        this.writeEnum(buf, state);
    }

    private void writeEnum(ByteBuf buf, Enum<?> e) {
        this.writeVarInt(buf, e.ordinal());
    }

    public Component readComponent(ByteBuf buf) {
        // do not use CompoundTag, as mojang serializes a plaintext component as just a single StringTag
        Tag tag = readAnyTag(buf, Tag.class);
        if (tag == null) {
            throw new IllegalArgumentException("Got end-tag when trying to read Component");
        }
        JsonElement json = ComponentSerializer.tagComponentToJson(tag);
        return DefaultComponentSerializer.get().deserializeFromTree(json);
    }

    public void writeComponent(ByteBuf buf, Component component) {
        JsonElement json = DefaultComponentSerializer.get().serializeToTree(component);
        Tag tag = ComponentSerializer.jsonComponentToTag(json);
        writeAnyTag(buf, tag);
    }

    public EntityMetadata<?, ?>[] readEntityMetadata(ByteBuf buf) {
        List<EntityMetadata<?, ?>> ret = new ArrayList<>();
        int id;
        while ((id = buf.readUnsignedByte()) != 255) {
            ret.add(this.readMetadata(buf, id));
        }

        return ret.toArray(new EntityMetadata<?, ?>[0]);
    }

    public void writeEntityMetadata(ByteBuf buf, EntityMetadata<?, ?>[] metadata) {
        for (EntityMetadata<?, ?> meta : metadata) {
            this.writeMetadata(buf, meta);
        }

        buf.writeByte(255);
    }

    public EntityMetadata<?, ?> readMetadata(ByteBuf buf, int id) {
        MetadataType<?> type = this.readMetadataType(buf);
        return type.readMetadata(this, buf, id);
    }

    public void writeMetadata(ByteBuf buf, EntityMetadata<?, ?> metadata) {
        buf.writeByte(metadata.getId());
        this.writeMetadataType(buf, metadata.getType());
        metadata.write(this, buf);
    }

    public MetadataType<?> readMetadataType(ByteBuf buf) {
        int id = this.readVarInt(buf);
        if (id >= MetadataTypes.size()) {
            throw new IllegalArgumentException("Received id " + id + " for MetadataType when the maximum was " + MetadataTypes.size() + "!");
        }

        return MetadataTypes.from(id);
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
        this.writeString(buf, pos.dimension());
        this.writePosition(buf, pos.position());
    }

    public PlayerSpawnInfo readPlayerSpawnInfo(ByteBuf buf) {
        String dimension = this.readString(buf);
        String worldName = this.readString(buf);
        long hashedSeed = buf.readLong();
        GameMode gameMode = GameMode.byId(buf.readByte());
        GameMode previousGamemode = GameMode.byNullableId(buf.readByte());
        boolean debug = buf.readBoolean();
        boolean flat = buf.readBoolean();
        GlobalPos lastDeathPos = this.readNullable(buf, this::readGlobalPos);
        int portalCooldown = this.readVarInt(buf);
        return new PlayerSpawnInfo(dimension, worldName, hashedSeed, gameMode, previousGamemode, debug, flat, lastDeathPos, portalCooldown);
    }

    public void writePlayerSpawnInfo(ByteBuf buf, PlayerSpawnInfo info) {
        this.writeString(buf, info.dimension());
        this.writeString(buf, info.worldName());
        buf.writeLong(info.hashedSeed());
        buf.writeByte(info.gameMode().ordinal());
        buf.writeByte(GameMode.toNullableId(info.previousGamemode()));
        buf.writeBoolean(info.debug());
        buf.writeBoolean(info.flat());
        this.writeNullable(buf, info.lastDeathPos(), this::writeGlobalPos);
        this.writeVarInt(buf, info.portalCooldown());
    }

    public ParticleType readParticleType(ByteBuf buf) {
        return ParticleType.from(this.readVarInt(buf));
    }

    public void writeParticleType(ByteBuf buf, ParticleType type) {
        this.writeEnum(buf, type);
    }

    public Particle readParticle(ByteBuf buf) {
        ParticleType particleType = this.readParticleType(buf);
        return new Particle(particleType, this.readParticleData(buf, particleType));
    }

    public void writeParticle(ByteBuf buf, Particle particle) {
        this.writeEnum(buf, particle.type());
        this.writeParticleData(buf, particle.type(), particle.data());
    }

    public ParticleData readParticleData(ByteBuf buf, ParticleType type) {
        return switch (type) {
            case BLOCK, BLOCK_MARKER -> new BlockParticleData(this.readVarInt(buf));
            case DUST -> {
                float red = buf.readFloat();
                float green = buf.readFloat();
                float blue = buf.readFloat();
                float scale = buf.readFloat();
                yield new DustParticleData(red, green, blue, scale);
            }
            case DUST_COLOR_TRANSITION -> {
                float red = buf.readFloat();
                float green = buf.readFloat();
                float blue = buf.readFloat();
                float scale = buf.readFloat();
                float newRed = buf.readFloat();
                float newGreen = buf.readFloat();
                float newBlue = buf.readFloat();
                yield new DustColorTransitionParticleData(red, green, blue, scale, newRed, newGreen, newBlue);
            }
            case FALLING_DUST -> new FallingDustParticleData(this.readVarInt(buf));
            case ITEM -> new ItemParticleData(this.readItemStack(buf));
            case SCULK_CHARGE -> new SculkChargeParticleData(buf.readFloat());
            case SHRIEK -> new ShriekParticleData(this.readVarInt(buf));
            case VIBRATION -> new VibrationParticleData(this.readPositionSource(buf), this.readVarInt(buf));
            default -> null;
        };
    }

    public void writeParticleData(ByteBuf buf, ParticleType type, ParticleData data) {
        switch (type) {
            case BLOCK:
            case BLOCK_MARKER:
                this.writeVarInt(buf, ((BlockParticleData) data).blockState());
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
                this.writeVarInt(buf, ((FallingDustParticleData) data).blockState());
                break;
            case ITEM:
                this.writeItemStack(buf, ((ItemParticleData) data).itemStack());
                break;
            case SCULK_CHARGE:
                buf.writeFloat(((SculkChargeParticleData) data).roll());
                break;
            case SHRIEK:
                this.writeVarInt(buf, ((ShriekParticleData) data).delay());
                break;
            case VIBRATION:
                this.writePositionSource(buf, ((VibrationParticleData) data).getPositionSource());
                this.writeVarInt(buf, ((VibrationParticleData) data).getArrivalTicks());
                break;
        }
    }

    public NumberFormat readNumberFormat(ByteBuf buf) {
        int id = this.readVarInt(buf);
        return switch (id) {
            case 0 -> BlankFormat.INSTANCE;
            case 1 -> new StyledFormat(this.readAnyTagOrThrow(buf));
            case 2 -> new FixedFormat(this.readComponent(buf));
            default -> throw new IllegalArgumentException("Unknown number format type: " + id);
        };
    }

    public void writeNumberFormat(ByteBuf buf, NumberFormat numberFormat) {
        if (numberFormat instanceof BlankFormat) {
            this.writeVarInt(buf, 0);
        } else if (numberFormat instanceof StyledFormat styledFormat) {
            this.writeVarInt(buf, 1);
            this.writeAnyTag(buf, styledFormat.style());
        } else if (numberFormat instanceof FixedFormat fixedFormat) {
            this.writeVarInt(buf, 2);
            this.writeComponent(buf, fixedFormat.value());
        } else {
            throw new IllegalArgumentException("Unknown number format: " + numberFormat);
        }
    }

    public PositionSource readPositionSource(ByteBuf buf) {
        PositionSourceType type = PositionSourceType.from(this.readVarInt(buf));
        return switch (type) {
            case BLOCK -> new BlockPositionSource(this.readPosition(buf));
            case ENTITY -> new EntityPositionSource(this.readVarInt(buf), buf.readFloat());
        };
    }

    public void writePositionSource(ByteBuf buf, PositionSource positionSource) {
        this.writeVarInt(buf, positionSource.getType().ordinal());
        if (positionSource instanceof BlockPositionSource) {
            this.writePosition(buf, ((BlockPositionSource) positionSource).position());
        } else if (positionSource instanceof EntityPositionSource) {
            this.writeVarInt(buf, ((EntityPositionSource) positionSource).getEntityId());
            buf.writeFloat(((EntityPositionSource) positionSource).getYOffset());
        } else {
            throw new IllegalStateException("Unknown position source type!");
        }
    }

    public VillagerData readVillagerData(ByteBuf buf) {
        return new VillagerData(this.readVarInt(buf), this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeVillagerData(ByteBuf buf, VillagerData villagerData) {
        this.writeVarInt(buf, villagerData.type());
        this.writeVarInt(buf, villagerData.profession());
        this.writeVarInt(buf, villagerData.level());
    }

    public ModifierOperation readModifierOperation(ByteBuf buf) {
        return ModifierOperation.from(buf.readByte());
    }

    public void writeModifierOperation(ByteBuf buf, ModifierOperation operation) {
        buf.writeByte(operation.ordinal());
    }

    public Effect readEffect(ByteBuf buf) {
        return Effect.from(this.readVarInt(buf));
    }

    public void writeEffect(ByteBuf buf, Effect effect) {
        this.writeVarInt(buf, effect.ordinal());
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

    @NotNull
    public BlockEntityType readBlockEntityType(ByteBuf buf) {
        int id = this.readVarInt(buf);
        BlockEntityType type = BlockEntityType.from(id);

        if (type == null) {
            throw new IllegalArgumentException("Unknown BlockEntityType: " + id);
        }
        return type;
    }

    public void writeBlockEntityType(ByteBuf buf, BlockEntityType type) {
        this.writeEnum(buf, type);
    }

    public LightUpdateData readLightUpdateData(ByteBuf buf) {
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

        return new LightUpdateData(skyYMask, blockYMask, emptySkyYMask, emptyBlockYMask, skyUpdates, blockUpdates);
    }

    public void writeLightUpdateData(ByteBuf buf, LightUpdateData data) {
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
        int id = buf.readInt();
        LevelEventType type = this.levelEvents.get(id);
        if (type != null) {
            return type;
        }
        return new UnknownLevelEvent(id);
    }

    public void writeLevelEvent(ByteBuf buf, LevelEvent event) {
        buf.writeInt(event.id());
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

    public Ingredient readRecipeIngredient(ByteBuf buf) {
        ItemStack[] options = new ItemStack[this.readVarInt(buf)];
        for (int i = 0; i < options.length; i++) {
            options[i] = this.readItemStack(buf);
        }

        return new Ingredient(options);
    }

    public void writeRecipeIngredient(ByteBuf buf, Ingredient ingredient) {
        this.writeVarInt(buf, ingredient.options().length);
        for (ItemStack option : ingredient.options()) {
            this.writeItemStack(buf, option);
        }
    }

    public DataPalette readDataPalette(ByteBuf buf, PaletteType paletteType, int globalPaletteBits) {
        int bitsPerEntry = buf.readByte() & 0xFF;
        Palette palette = this.readPalette(buf, paletteType, bitsPerEntry);
        BitStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), this.readLongArray(buf));
        } else {
            // Eat up - can be seen on Hypixel as of 1.19.0
            int length = this.readVarInt(buf);
            for (int i = 0; i < length; i++) {
                buf.readLong();
            }
            storage = null;
        }

        return new DataPalette(paletteType, globalPaletteBits, palette, storage);
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

    private Palette readPalette(ByteBuf buf, PaletteType paletteType, int bitsPerEntry) {
        if (bitsPerEntry == 0) {
            return new SingletonPalette(this.readVarInt(buf));
        }
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry, buf, this);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntry()) {
            return new MapPalette(bitsPerEntry, buf, this);
        } else {
            return GlobalPalette.INSTANCE;
        }
    }

    public ChunkSection readChunkSection(ByteBuf buf, int globalBiomePaletteBits) {
        int blockCount = buf.readShort();

        DataPalette chunkPalette = this.readDataPalette(buf, PaletteType.CHUNK, DataPalette.GLOBAL_PALETTE_BITS_PER_ENTRY);
        DataPalette biomePalette = this.readDataPalette(buf, PaletteType.BIOME, globalBiomePaletteBits);
        return new ChunkSection(chunkPalette, biomePalette, blockCount);
    }

    public void writeChunkSection(ByteBuf buf, ChunkSection section) {
        buf.writeShort(section.getBlockCount());
        this.writeDataPalette(buf, section.getChunkData());
        this.writeDataPalette(buf, section.getBiomeData());
    }

    public <E extends Enum<E>> EnumSet<E> readEnumSet(ByteBuf buf, E[] values) {
        BitSet bitSet = this.readFixedBitSet(buf, values.length);
        List<E> readValues = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            if (bitSet.get(i)) {
                readValues.add(values[i]);
            }
        }

        return EnumSet.copyOf(readValues);
    }

    public <E extends Enum<E>> void writeEnumSet(ByteBuf buf, EnumSet<E> enumSet, E[] values) {
        BitSet bitSet = new BitSet(values.length);

        for (int i = 0; i < values.length; i++) {
            bitSet.set(i, enumSet.contains(values[i]));
        }

        this.writeFixedBitSet(buf, bitSet, values.length);
    }

    public BitSet readFixedBitSet(ByteBuf buf, int length) {
        byte[] bytes = new byte[-Math.floorDiv(-length, 8)];
        buf.readBytes(bytes);
        return BitSet.valueOf(bytes);
    }

    public void writeFixedBitSet(ByteBuf buf, BitSet bitSet, int length) {
        if (bitSet.length() > length) {
            throw new IllegalArgumentException("BitSet is larger than expected size (" + bitSet.length() + " > " + length + ")");
        } else {
            byte[] bytes = bitSet.toByteArray();
            buf.writeBytes(Arrays.copyOf(bytes, -Math.floorDiv(-length, 8)));
        }
    }

    public GameProfile.Property readProperty(ByteBuf buf) {
        String name = this.readString(buf);
        String value = this.readString(buf);
        String signature = this.readNullable(buf, this::readString);
        return new GameProfile.Property(name, value, signature);
    }

    public void writeProperty(ByteBuf buf, GameProfile.Property property) {
        this.writeString(buf, property.name());
        this.writeString(buf, property.value());
        this.writeNullable(buf, property.signature(), this::writeString);
    }

    public <T> T readById(ByteBuf buf, IntFunction<T> registry, Function<ByteBuf, T> custom) {
        int id = this.readVarInt(buf);
        if (id == 0) {
            return custom.apply(buf);
        }
        return registry.apply(id - 1);
    }

    public CustomSound readSoundEvent(ByteBuf buf) {
        String name = this.readString(buf);
        boolean isNewSystem = buf.readBoolean();
        return new CustomSound(name, isNewSystem, isNewSystem ? buf.readFloat() : 16.0F);
    }

    public void writeSoundEvent(ByteBuf buf, Sound soundEvent) {
        writeString(buf, soundEvent.name());
        buf.writeBoolean(soundEvent.newSystem());
        if (soundEvent.newSystem()) {
            buf.writeFloat(soundEvent.range());
        }
    }

    public NibbleArray3d readNibbleArray(ByteBuf buf, int size) {
        return new NibbleArray3d(this.readByteArray(buf, ignored -> size));
    }

    public void writeNibbleArray(ByteBuf buf, NibbleArray3d nibbleArray) {
        buf.writeBytes(nibbleArray.getData());
    }
}
