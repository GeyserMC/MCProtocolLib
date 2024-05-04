package org.geysermc.mcprotocollib.protocol.codec;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.network.codec.BaseCodecByteBuf;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.Identifier;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.BlankFormat;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.FixedFormat;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.NumberFormat;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.StyledFormat;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.BitStorage;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkSection;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.DataPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.GlobalPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.ListPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.MapPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.Palette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.PaletteType;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.SingletonPalette;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.ArmadilloState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Pose;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.SnifferState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.VillagerData;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.BlockBreakStage;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.PaintingType;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemCodecByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEventType;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.UnknownLevelEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.BlockParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.DustColorTransitionParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.DustParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.EntityEffectParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ItemParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.SculkChargeParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ShriekParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.VibrationParticleData;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.BlockPositionSource;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.EntityPositionSource;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.PositionSource;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.PositionSourceType;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.SoundCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.StatisticCategory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class MinecraftByteBuf extends BaseCodecByteBuf {
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 12;
    private static final int POSITION_Z_SIZE = 38;
    private static final int POSITION_Y_SHIFT = 0xFFF;
    private static final int POSITION_WRITE_SHIFT = 0x3FFFFFF;

    private final Int2ObjectMap<LevelEventType> levelEvents;
    private final Map<String, BuiltinSound> soundNames;

    public MinecraftByteBuf(Int2ObjectMap<LevelEventType> levelEvents, Map<String, BuiltinSound> soundNames, ByteBuf buf) {
        super(buf);
        this.levelEvents = levelEvents;
        this.soundNames = soundNames;
    }

    @Nullable
    public <T> T readNullable(Supplier<T> ifPresent) {
        if (this.readBoolean()) {
            return ifPresent.get();
        } else {
            return null;
        }
    }

    public <T> void writeNullable(@Nullable T value, Consumer<T> ifPresent) {
        if (value != null) {
            this.writeBoolean(true);
            ifPresent.accept(value);
        } else {
            this.writeBoolean(false);
        }
    }

    public <T> Holder<T> readHolder(Supplier<T> readCustom) {
        int registryId = this.readVarInt();
        return registryId == 0 ? Holder.ofCustom(readCustom.get()) : Holder.ofId(registryId - 1);
    }

    public <T> void writeHolder(Holder<T> holder, Consumer<T> writeCustom) {
        if (holder.isCustom()) {
            this.writeVarInt(0);
            writeCustom.accept(holder.custom());
        } else {
            this.writeVarInt(holder.id() + 1);
        }
    }

    public String readResourceLocation() {
        return Identifier.formalize(this.readString());
    }

    public void writeResourceLocation(String location) {
        this.writeString(location);
    }

    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    public void writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    public byte[] readByteArray() {
        return this.readByteArray(this::readVarInt);
    }

    public byte[] readByteArray(IntSupplier reader) {
        int length = reader.getAsInt();
        byte[] bytes = new byte[length];
        this.readBytes(bytes);
        return bytes;
    }

    public void writeByteArray(byte[] bytes) {
        this.writeByteArray(bytes, this::writeVarInt);
    }

    public void writeByteArray(byte[] bytes, IntConsumer writer) {
        writer.accept(bytes.length);
        this.writeBytes(bytes);
    }

    public long[] readLongArray() {
        return this.readLongArray(this::readVarInt);
    }

    public long[] readLongArray(IntSupplier reader) {
        int length = reader.getAsInt();
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }

        long[] l = new long[length];
        for (int index = 0; index < length; index++) {
            l[index] = this.readLong();
        }

        return l;
    }

    public void writeLongArray(long[] l) {
        this.writeLongArray(l, this::writeVarInt);
    }

    public void writeLongArray(long[] l, IntConsumer writer) {
        writer.accept(l.length);
        for (long value : l) {
            this.writeLong(value);
        }
    }

    @Nullable
    public NbtMap readCompoundTag() {
        return readAnyTag(NbtType.COMPOUND);
    }

    @NonNull
    public NbtMap readCompoundTagOrThrow() {
        NbtMap tag = readCompoundTag();
        if (tag == null) {
            throw new IllegalArgumentException("Got end-tag when trying to read NbtMap");
        }
        return tag;
    }

    @Nullable
    public <T> T readAnyTag(NbtType<T> expected) {
        Object tag = this.readAnyTag();

        if (tag == null) {
            return null;
        }

        Class<T> tagClass = expected.getTagClass();
        if (!tagClass.isInstance(tag)) {
            throw new IllegalArgumentException("Expected tag of type " + tagClass.getName() + " but got " + tag.getClass().getName());
        }

        return tagClass.cast(tag);
    }

    @Nullable
    public Object readAnyTag() {
        try {
            ByteBufInputStream input = new ByteBufInputStream(this);

            int typeId = input.readUnsignedByte();
            if (typeId == 0) {
                return null;
            }

            NbtType<?> type = NbtType.byId(typeId);

            return new NBTInputStream(input).readValue(type);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void writeAnyTag(@Nullable Object tag) {
        try {
            ByteBufOutputStream output = new ByteBufOutputStream(this);

            if (tag == null) {
                output.writeByte(0);
                return;
            }

            NbtType<?> type = NbtType.byClass(tag.getClass());
            output.writeByte(type.getId());

            new NBTOutputStream(output).writeValue(tag);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    public ItemStack readOptionalItemStack() {
        byte count = this.readByte();
        if (count <= 0) {
            return null;
        }

        int item = this.readVarInt();
        return new ItemStack(item, count, this.readDataComponentPatch());
    }

    public void writeOptionalItemStack(ItemStack item) {
        boolean empty = item == null || item.getAmount() <= 0;
        this.writeByte(!empty ? item.getAmount() : 0);
        if (!empty) {
            this.writeVarInt(item.getId());
            this.writeDataComponentPatch(item.getDataComponents());
        }
    }

    @NotNull
    public ItemStack readItemStack() {
        return this.readOptionalItemStack();
    }

    public void writeItemStack(@NotNull ItemStack item) {
        this.writeOptionalItemStack(item);
    }

    @Nullable
    public DataComponents readDataComponentPatch() {
        int nonNullComponents = this.readVarInt();
        int nullComponents = this.readVarInt();
        if (nonNullComponents == 0 && nullComponents == 0) {
            return null;
        }

        Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents = new HashMap<>();
        for (int k = 0; k < nonNullComponents; k++) {
            DataComponentType<?> dataComponentType = DataComponentType.from(this.readVarInt());
            DataComponent<?, ?> dataComponent = dataComponentType.readDataComponent(new ItemCodecByteBuf(this));
            dataComponents.put(dataComponentType, dataComponent);
        }

        for (int k = 0; k < nullComponents; k++) {
            DataComponentType<?> dataComponentType = DataComponentType.from(this.readVarInt());
            DataComponent<?, ?> dataComponent = dataComponentType.readNullDataComponent();
            dataComponents.put(dataComponentType, dataComponent);
        }

        return new DataComponents(dataComponents);
    }

    public void writeDataComponentPatch(DataComponents dataComponents) {
        if (dataComponents == null) {
            this.writeVarInt(0);
            this.writeVarInt(0);
        } else {
            int i = 0;
            int j = 0;
            for (DataComponent<?, ?> component : dataComponents.getDataComponents().values()) {
                if (component.getValue() != null) {
                    i++;
                } else {
                    j++;
                }
            }

            this.writeVarInt(i);
            this.writeVarInt(j);

            for (DataComponent<?, ?> component : dataComponents.getDataComponents().values()) {
                if (component.getValue() != null) {
                    this.writeVarInt(component.getType().getId());
                    component.write(new ItemCodecByteBuf(this));
                }
            }

            for (DataComponent<?, ?> component : dataComponents.getDataComponents().values()) {
                if (component.getValue() == null) {
                    this.writeVarInt(component.getType().getId());
                }
            }
        }
    }

    @NotNull
    public ItemStack readTradeItemStack() {
        int item = this.readVarInt();
        int count = this.readVarInt();
        int componentsLength = this.readVarInt();

        Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents = new HashMap<>();
        for (int i = 0; i < componentsLength; i++) {
            DataComponentType<?> dataComponentType = DataComponentType.from(this.readVarInt());
            DataComponent<?, ?> dataComponent = dataComponentType.readDataComponent(new ItemCodecByteBuf(this));
            dataComponents.put(dataComponentType, dataComponent);
        }

        return new ItemStack(item, count, new DataComponents(dataComponents));
    }

    public void writeTradeItemStack(@NotNull ItemStack item) {
        this.writeVarInt(item.getId());
        this.writeVarInt(item.getAmount());

        DataComponents dataComponents = item.getDataComponents();
        if (dataComponents == null) {
            this.writeVarInt(0);
            return;
        }

        this.writeVarInt(dataComponents.getDataComponents().size());
        for (DataComponent<?, ?> component : dataComponents.getDataComponents().values()) {
            this.writeVarInt(component.getType().getId());
            component.write(new ItemCodecByteBuf(this));
        }
    }

    public Vector3i readPosition() {
        long val = this.readLong();

        int x = (int) (val >> POSITION_X_SIZE);
        int y = (int) (val << 52 >> 52);
        int z = (int) (val << 26 >> POSITION_Z_SIZE);

        return Vector3i.from(x, y, z);
    }

    public void writePosition(Vector3i pos) {
        long x = pos.getX() & POSITION_WRITE_SHIFT;
        long y = pos.getY() & POSITION_Y_SHIFT;
        long z = pos.getZ() & POSITION_WRITE_SHIFT;

        this.writeLong(x << POSITION_X_SIZE | z << POSITION_Y_SIZE | y);
    }

    public Vector3f readRotation() {
        float x = this.readFloat();
        float y = this.readFloat();
        float z = this.readFloat();

        return Vector3f.from(x, y, z);
    }

    public void writeRotation(Vector3f rot) {
        this.writeFloat(rot.getX());
        this.writeFloat(rot.getY());
        this.writeFloat(rot.getZ());
    }

    public Vector4f readQuaternion() {
        float x = this.readFloat();
        float y = this.readFloat();
        float z = this.readFloat();
        float w = this.readFloat();

        return Vector4f.from(x, y, z, w);
    }

    public void writeQuaternion(Vector4f vec4) {
        this.writeFloat(vec4.getX());
        this.writeFloat(vec4.getY());
        this.writeFloat(vec4.getZ());
        this.writeFloat(vec4.getW());
    }

    public Direction readDirection() {
        return Direction.from(this.readVarInt());
    }

    public void writeDirection(Direction dir) {
        this.writeEnum(dir);
    }

    public Pose readPose() {
        return Pose.from(this.readVarInt());
    }

    public void writePose(Pose pose) {
        this.writeEnum(pose);
    }

    public PaintingType readPaintingType() {
        return PaintingType.from(this.readVarInt());
    }

    public void writePaintingType(PaintingType type) {
        this.writeEnum(type);
    }

    public SnifferState readSnifferState() {
        return SnifferState.from(this.readVarInt());
    }

    public void writeSnifferState(SnifferState state) {
        this.writeEnum(state);
    }

    public ArmadilloState readArmadilloState() {
        return ArmadilloState.from(this.readVarInt());
    }

    public void writeArmadilloState(ArmadilloState state) {
        this.writeEnum(state);
    }

    private void writeEnum(Enum<?> e) {
        this.writeVarInt(e.ordinal());
    }

    public Component readComponent() {
        // do not use NbtMap, as mojang serializes a plaintext component as just a single StringTag
        Object tag = readAnyTag();
        if (tag == null) {
            throw new IllegalArgumentException("Got end-tag when trying to read Component");
        }
        JsonElement json = NbtComponentSerializer.tagComponentToJson(tag);
        return DefaultComponentSerializer.get().deserializeFromTree(json);
    }

    public void writeComponent(Component component) {
        JsonElement json = DefaultComponentSerializer.get().serializeToTree(component);
        Object tag = NbtComponentSerializer.jsonComponentToTag(json);
        writeAnyTag(tag);
    }

    public EntityMetadata<?, ?>[] readEntityMetadata() {
        List<EntityMetadata<?, ?>> ret = new ArrayList<>();
        int id;
        while ((id = this.readUnsignedByte()) != 255) {
            ret.add(this.readMetadata(id));
        }

        return ret.toArray(new EntityMetadata<?, ?>[0]);
    }

    public void writeEntityMetadata(EntityMetadata<?, ?>[] metadata) {
        for (EntityMetadata<?, ?> meta : metadata) {
            this.writeMetadata(meta);
        }

        this.writeByte(255);
    }

    public EntityMetadata<?, ?> readMetadata(int id) {
        MetadataType<?> type = this.readMetadataType();
        return type.readMetadata(this, id);
    }

    public void writeMetadata(EntityMetadata<?, ?> metadata) {
        this.writeByte(metadata.getId());
        this.writeMetadataType(metadata.getType());
        metadata.write(this);
    }

    public MetadataType<?> readMetadataType() {
        int id = this.readVarInt();
        if (id >= MetadataType.size()) {
            throw new IllegalArgumentException("Received id " + id + " for MetadataType when the maximum was " + MetadataType.size() + "!");
        }

        return MetadataType.from(id);
    }

    public void writeMetadataType(MetadataType<?> type) {
        this.writeVarInt(type.getId());
    }

    public GlobalPos readGlobalPos() {
        String dimension = Identifier.formalize(this.readString());
        Vector3i pos = this.readPosition();
        return new GlobalPos(dimension, pos);
    }

    public void writeGlobalPos(GlobalPos pos) {
        this.writeString(pos.getDimension());
        this.writePosition(pos.getPosition());
    }

    public PlayerSpawnInfo readPlayerSpawnInfo() {
        int dimension = this.readVarInt();
        String worldName = this.readString();
        long hashedSeed = this.readLong();
        GameMode gameMode = GameMode.byId(this.readByte());
        GameMode previousGamemode = GameMode.byNullableId(this.readByte());
        boolean debug = this.readBoolean();
        boolean flat = this.readBoolean();
        GlobalPos lastDeathPos = this.readNullable(this::readGlobalPos);
        int portalCooldown = this.readVarInt();
        return new PlayerSpawnInfo(dimension, worldName, hashedSeed, gameMode, previousGamemode, debug, flat, lastDeathPos, portalCooldown);
    }

    public void writePlayerSpawnInfo(PlayerSpawnInfo info) {
        this.writeVarInt(info.getDimension());
        this.writeString(info.getWorldName());
        this.writeLong(info.getHashedSeed());
        this.writeByte(info.getGameMode().ordinal());
        this.writeByte(GameMode.toNullableId(info.getPreviousGamemode()));
        this.writeBoolean(info.isDebug());
        this.writeBoolean(info.isFlat());
        this.writeNullable(info.getLastDeathPos(), this::writeGlobalPos);
        this.writeVarInt(info.getPortalCooldown());
    }

    public ParticleType readParticleType() {
        return ParticleType.from(this.readVarInt());
    }

    public void writeParticleType(ParticleType type) {
        this.writeEnum(type);
    }

    public Particle readParticle() {
        ParticleType particleType = this.readParticleType();
        return new Particle(particleType, this.readParticleData(particleType));
    }

    public void writeParticle(Particle particle) {
        this.writeEnum(particle.getType());
        this.writeParticleData(particle.getType(), particle.getData());
    }

    public ParticleData readParticleData(ParticleType type) {
        return switch (type) {
            case BLOCK, BLOCK_MARKER, FALLING_DUST, DUST_PILLAR -> new BlockParticleData(this.readVarInt());
            case DUST -> {
                float red = this.readFloat();
                float green = this.readFloat();
                float blue = this.readFloat();
                float scale = this.readFloat();
                yield new DustParticleData(red, green, blue, scale);
            }
            case DUST_COLOR_TRANSITION -> {
                float red = this.readFloat();
                float green = this.readFloat();
                float blue = this.readFloat();
                float newRed = this.readFloat();
                float newGreen = this.readFloat();
                float newBlue = this.readFloat();
                float scale = this.readFloat();
                yield new DustColorTransitionParticleData(red, green, blue, scale, newRed, newGreen, newBlue);
            }
            case ENTITY_EFFECT -> new EntityEffectParticleData(this.readInt());
            case ITEM -> new ItemParticleData(this.readOptionalItemStack());
            case SCULK_CHARGE -> new SculkChargeParticleData(this.readFloat());
            case SHRIEK -> new ShriekParticleData(this.readVarInt());
            case VIBRATION -> new VibrationParticleData(this.readPositionSource(), this.readVarInt());
            default -> null;
        };
    }

    public void writeParticleData(ParticleType type, ParticleData data) {
        switch (type) {
            case BLOCK, BLOCK_MARKER, FALLING_DUST, DUST_PILLAR -> {
                BlockParticleData blockData = (BlockParticleData) data;
                this.writeVarInt(blockData.getBlockState());
            }
            case DUST -> {
                DustParticleData dustData = (DustParticleData) data;
                this.writeFloat(dustData.getRed());
                this.writeFloat(dustData.getGreen());
                this.writeFloat(dustData.getBlue());
                this.writeFloat(dustData.getScale());
            }
            case DUST_COLOR_TRANSITION -> {
                DustColorTransitionParticleData dustData = (DustColorTransitionParticleData) data;
                this.writeFloat(dustData.getRed());
                this.writeFloat(dustData.getGreen());
                this.writeFloat(dustData.getBlue());
                this.writeFloat(dustData.getNewRed());
                this.writeFloat(dustData.getNewGreen());
                this.writeFloat(dustData.getNewBlue());
                this.writeFloat(dustData.getScale());
            }
            case ENTITY_EFFECT -> {
                EntityEffectParticleData entityEffectData = (EntityEffectParticleData) data;
                this.writeInt(entityEffectData.getColor());
            }
            case ITEM -> {
                ItemParticleData itemData = (ItemParticleData) data;
                this.writeOptionalItemStack(itemData.getItemStack());
            }
            case SCULK_CHARGE -> {
                SculkChargeParticleData sculkData = (SculkChargeParticleData) data;
                this.writeFloat(sculkData.getRoll());
            }
            case SHRIEK -> {
                ShriekParticleData shriekData = (ShriekParticleData) data;
                this.writeVarInt(shriekData.getDelay());
            }
            case VIBRATION -> {
                VibrationParticleData vibrationData = (VibrationParticleData) data;
                this.writePositionSource(vibrationData.getPositionSource());
                this.writeVarInt(vibrationData.getArrivalTicks());
            }
        }
    }

    public NumberFormat readNumberFormat() {
        int id = this.readVarInt();
        return switch (id) {
            case 0 -> BlankFormat.INSTANCE;
            case 1 -> new StyledFormat(this.readCompoundTagOrThrow());
            case 2 -> new FixedFormat(this.readComponent());
            default -> throw new IllegalArgumentException("Unknown number format type: " + id);
        };
    }

    public void writeNumberFormat(NumberFormat numberFormat) {
        if (numberFormat instanceof BlankFormat) {
            this.writeVarInt(0);
        } else if (numberFormat instanceof StyledFormat styledFormat) {
            this.writeVarInt(1);
            this.writeAnyTag(styledFormat.getStyle());
        } else if (numberFormat instanceof FixedFormat fixedFormat) {
            this.writeVarInt(2);
            this.writeComponent(fixedFormat.getValue());
        } else {
            throw new IllegalArgumentException("Unknown number format: " + numberFormat);
        }
    }

    public PositionSource readPositionSource() {
        PositionSourceType type = PositionSourceType.from(this.readVarInt());
        return switch (type) {
            case BLOCK -> new BlockPositionSource(this.readPosition());
            case ENTITY -> new EntityPositionSource(this.readVarInt(), this.readFloat());
        };
    }

    public void writePositionSource(PositionSource positionSource) {
        this.writeVarInt(positionSource.getType().ordinal());
        if (positionSource instanceof BlockPositionSource blockPositionSource) {
            this.writePosition(blockPositionSource.getPosition());
        } else if (positionSource instanceof EntityPositionSource entityPositionSource) {
            this.writeVarInt(entityPositionSource.getEntityId());
            this.writeFloat(entityPositionSource.getYOffset());
        } else {
            throw new IllegalStateException("Unknown position source type!");
        }
    }

    public VillagerData readVillagerData() {
        return new VillagerData(this.readVarInt(), this.readVarInt(), this.readVarInt());
    }

    public void writeVillagerData(VillagerData villagerData) {
        this.writeVarInt(villagerData.getType());
        this.writeVarInt(villagerData.getProfession());
        this.writeVarInt(villagerData.getLevel());
    }

    public ModifierOperation readModifierOperation() {
        return ModifierOperation.from(this.readByte());
    }

    public void writeModifierOperation(ModifierOperation operation) {
        this.writeByte(operation.ordinal());
    }

    public Effect readEffect() {
        return Effect.from(this.readVarInt());
    }

    public void writeEffect(Effect effect) {
        this.writeVarInt(effect.ordinal());
    }

    public BlockBreakStage readBlockBreakStage() {
        int stage = this.readUnsignedByte();
        if (stage >= 0 && stage < 10) {
            return BlockBreakStage.STAGES[stage];
        } else {
            return BlockBreakStage.RESET;
        }
    }

    public void writeBlockBreakStage(BlockBreakStage stage) {
        if (stage == BlockBreakStage.RESET) {
            this.writeByte(255);
        } else {
            this.writeByte(stage.ordinal());
        }
    }

    @Nullable
    public BlockEntityType readBlockEntityType() {
        return BlockEntityType.from(this.readVarInt());
    }

    public void writeBlockEntityType(BlockEntityType type) {
        this.writeEnum(type);
    }

    public LightUpdateData readLightUpdateData() {
        BitSet skyYMask = BitSet.valueOf(this.readLongArray());
        BitSet blockYMask = BitSet.valueOf(this.readLongArray());
        BitSet emptySkyYMask = BitSet.valueOf(this.readLongArray());
        BitSet emptyBlockYMask = BitSet.valueOf(this.readLongArray());

        int skyUpdateSize = this.readVarInt();
        List<byte[]> skyUpdates = new ArrayList<>(skyUpdateSize);
        for (int i = 0; i < skyUpdateSize; i++) {
            skyUpdates.add(this.readByteArray());
        }

        int blockUpdateSize = this.readVarInt();
        List<byte[]> blockUpdates = new ArrayList<>(blockUpdateSize);
        for (int i = 0; i < blockUpdateSize; i++) {
            blockUpdates.add(this.readByteArray());
        }

        return new LightUpdateData(skyYMask, blockYMask, emptySkyYMask, emptyBlockYMask, skyUpdates, blockUpdates);
    }

    public void writeLightUpdateData(LightUpdateData data) {
        writeBitSet(data.getSkyYMask());
        writeBitSet(data.getBlockYMask());
        writeBitSet(data.getEmptySkyYMask());
        writeBitSet(data.getEmptyBlockYMask());

        this.writeVarInt(data.getSkyUpdates().size());
        for (byte[] array : data.getSkyUpdates()) {
            this.writeByteArray(array);
        }

        this.writeVarInt(data.getBlockUpdates().size());
        for (byte[] array : data.getBlockUpdates()) {
            this.writeByteArray(array);
        }
    }

    private void writeBitSet(BitSet bitSet) {
        long[] array = bitSet.toLongArray();
        this.writeLongArray(array);
    }

    public LevelEvent readLevelEvent() {
        int id = this.readInt();
        LevelEventType type = this.levelEvents.get(id);
        if (type != null) {
            return type;
        }
        return new UnknownLevelEvent(id);
    }

    public void writeLevelEvent(LevelEvent event) {
        this.writeInt(event.getId());
    }

    public StatisticCategory readStatisticCategory() {
        return StatisticCategory.from(this.readVarInt());
    }

    public void writeStatisticCategory(StatisticCategory category) {
        this.writeEnum(category);
    }

    public SoundCategory readSoundCategory() {
        return SoundCategory.from(this.readVarInt());
    }

    public void writeSoundCategory(SoundCategory category) {
        this.writeEnum(category);
    }

    @Nullable
    public BuiltinSound getBuiltinSound(String name) {
        return this.soundNames.get(name);
    }

    public EntityEvent readEntityEvent() {
        return EntityEvent.from(this.readByte());
    }

    public void writeEntityEvent(EntityEvent event) {
        this.writeByte(event.ordinal());
    }

    public Ingredient readRecipeIngredient() {
        ItemStack[] options = new ItemStack[this.readVarInt()];
        for (int i = 0; i < options.length; i++) {
            options[i] = this.readOptionalItemStack();
        }

        return new Ingredient(options);
    }

    public void writeRecipeIngredient(Ingredient ingredient) {
        this.writeVarInt(ingredient.getOptions().length);
        for (ItemStack option : ingredient.getOptions()) {
            this.writeOptionalItemStack(option);
        }
    }

    public DataPalette readDataPalette(PaletteType paletteType) {
        int bitsPerEntry = this.readByte() & 0xFF;
        Palette palette = this.readPalette(paletteType, bitsPerEntry);
        BitStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), this.readLongArray());
        } else {
            // Eat up - can be seen on Hypixel as of 1.19.0
            int length = this.readVarInt();
            for (int i = 0; i < length; i++) {
                this.readLong();
            }
            storage = null;
        }

        return new DataPalette(palette, storage, paletteType);
    }

    /**
     * @deprecated globalPaletteBits is no longer in use, use {@link #readDataPalette(PaletteType)} instead.
     */
    @Deprecated(forRemoval = true)
    public DataPalette readDataPalette(PaletteType paletteType, int globalPaletteBits) {
        return this.readDataPalette(paletteType);
    }

    public void writeDataPalette(DataPalette palette) {
        if (palette.getPalette() instanceof SingletonPalette) {
            this.writeByte(0); // Bits per entry
            this.writeVarInt(palette.getPalette().idToState(0));
            this.writeVarInt(0); // Data length
            return;
        }

        this.writeByte(palette.getStorage().getBitsPerEntry());

        if (!(palette.getPalette() instanceof GlobalPalette)) {
            int paletteLength = palette.getPalette().size();
            this.writeVarInt(paletteLength);
            for (int i = 0; i < paletteLength; i++) {
                this.writeVarInt(palette.getPalette().idToState(i));
            }
        }

        long[] data = palette.getStorage().getData();
        this.writeLongArray(data);
    }

    private Palette readPalette(PaletteType paletteType, int bitsPerEntry) {
        if (bitsPerEntry == 0) {
            return new SingletonPalette(this.readVarInt());
        }
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry, this);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntry()) {
            return new MapPalette(bitsPerEntry, this);
        } else {
            return GlobalPalette.INSTANCE;
        }
    }

    public ChunkSection readChunkSection() {
        int blockCount = this.readShort();

        DataPalette chunkPalette = this.readDataPalette(PaletteType.CHUNK);
        DataPalette biomePalette = this.readDataPalette(PaletteType.BIOME);
        return new ChunkSection(blockCount, chunkPalette, biomePalette);
    }

    /**
     * @deprecated globalBiomePaletteBits is no longer in use, use {@link #readChunkSection()} instead.
     */
    @Deprecated(forRemoval = true)
    public ChunkSection readChunkSection(int globalBiomePaletteBits) {
        return this.readChunkSection();
    }

    public void writeChunkSection(ChunkSection section) {
        this.writeShort(section.getBlockCount());
        this.writeDataPalette(section.getChunkData());
        this.writeDataPalette(section.getBiomeData());
    }

    public <E extends Enum<E>> EnumSet<E> readEnumSet(E[] values) {
        BitSet bitSet = this.readFixedBitSet(values.length);
        List<E> readValues = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            if (bitSet.get(i)) {
                readValues.add(values[i]);
            }
        }

        return EnumSet.copyOf(readValues);
    }

    public <E extends Enum<E>> void writeEnumSet(EnumSet<E> enumSet, E[] values) {
        BitSet bitSet = new BitSet(values.length);

        for (int i = 0; i < values.length; i++) {
            bitSet.set(i, enumSet.contains(values[i]));
        }

        this.writeFixedBitSet(bitSet, values.length);
    }

    public BitSet readFixedBitSet(int length) {
        byte[] bytes = new byte[-Math.floorDiv(-length, 8)];
        this.readBytes(bytes);
        return BitSet.valueOf(bytes);
    }

    public void writeFixedBitSet(BitSet bitSet, int length) {
        if (bitSet.length() > length) {
            throw new IllegalArgumentException("BitSet is larger than expected size (" + bitSet.length() + " > " + length + ")");
        } else {
            byte[] bytes = bitSet.toByteArray();
            this.writeBytes(Arrays.copyOf(bytes, -Math.floorDiv(-length, 8)));
        }
    }

    public GameProfile.Property readProperty() {
        String name = this.readString();
        String value = this.readString();
        String signature = this.readNullable(this::readString);
        return new GameProfile.Property(name, value, signature);
    }

    public void writeProperty(GameProfile.Property property) {
        this.writeString(property.getName());
        this.writeString(property.getValue());
        this.writeNullable(property.getSignature(), this::writeString);
    }

    public <T> T readById(IntFunction<T> registry, Supplier<T> custom) {
        int id = this.readVarInt();
        if (id == 0) {
            return custom.get();
        }
        return registry.apply(id - 1);
    }

    public CustomSound readSoundEvent() {
        String name = this.readString();
        boolean isNewSystem = this.readBoolean();
        return new CustomSound(name, isNewSystem, isNewSystem ? this.readFloat() : 16.0F);
    }

    public void writeSoundEvent(Sound soundEvent) {
        writeString(soundEvent.getName());
        this.writeBoolean(soundEvent.isNewSystem());
        if (soundEvent.isNewSystem()) {
            this.writeFloat(soundEvent.getRange());
        }
    }
}
