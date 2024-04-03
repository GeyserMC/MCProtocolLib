package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import com.github.steveice10.mc.protocol.data.game.item.component.type.BooleanDataComponent;
import com.github.steveice10.mc.protocol.data.game.item.component.type.IntDataComponent;
import com.github.steveice10.mc.protocol.data.game.item.component.type.ObjectDataComponent;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DataComponentType<T> {
    private static final List<DataComponentType<?>> VALUES = new ArrayList<>();

    public static final DataComponentType<CompoundTag> CUSTOM_DATA = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAX_STACK_SIZE = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final IntComponentType MAX_DAMAGE = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final IntComponentType DAMAGE = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final BooleanComponentType UNBREAKABLE = new BooleanComponentType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<Component> CUSTOM_NAME = new DataComponentType<>(ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Component> ITEM_NAME = new DataComponentType<>(ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<List<Component>> LORE = new DataComponentType<>(listReader(ItemCodecHelper::readComponent), listWriter(ItemCodecHelper::writeComponent), ObjectDataComponent::new);
    public static final IntComponentType RARITY = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = new DataComponentType<>(ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = new DataComponentType<>(ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_BREAK = new DataComponentType<>(ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = new DataComponentType<>(ItemCodecHelper::readItemAttributeModifiers, ItemCodecHelper::writeItemAttributeModifiers, ObjectDataComponent::new);
    public static final IntComponentType CUSTOM_MODEL_DATA = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Void> HIDE_ADDITIONAL_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Void> HIDE_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final IntComponentType REPAIR_COST = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Void> CREATIVE_SLOT_LOCK = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = new BooleanComponentType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<CompoundTag> INTANGIBLE_PROJECTILE = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<FoodProperties> FOOD = new DataComponentType<>(ItemCodecHelper::readFoodProperties, ItemCodecHelper::writeFoodProperties, ObjectDataComponent::new);
    public static final DataComponentType<Void> FIRE_RESISTANT = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<ToolData> TOOL = new DataComponentType<>(ItemCodecHelper::readToolData, ItemCodecHelper::writeToolData, ObjectDataComponent::new);
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentType<>(ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<DyedItemColor> DYED_COLOR = new DataComponentType<>(ItemCodecHelper::readDyedItemColor, ItemCodecHelper::writeDyedItemColor, ObjectDataComponent::new);
    public static final IntComponentType MAP_COLOR = new IntComponentType((helper, input) -> input.readInt(), (helper, output, value) -> output.writeInt(value), IntDataComponent::new);
    public static final IntComponentType MAP_ID = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<CompoundTag> MAP_DECORATIONS = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAP_POST_PROCESSING = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = new DataComponentType<>(listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = new DataComponentType<>(listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<PotionContents> POTION_CONTENTS = new DataComponentType<>(ItemCodecHelper::readPotionContents, ItemCodecHelper::writePotionContents, ObjectDataComponent::new);
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = new DataComponentType<>(listReader(ItemCodecHelper::readStewEffect), listWriter(ItemCodecHelper::writeStewEffect), ObjectDataComponent::new);
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = new DataComponentType<>(ItemCodecHelper::readWritableBookContent, ItemCodecHelper::writeWritableBookContent, ObjectDataComponent::new);
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = new DataComponentType<>(ItemCodecHelper::readWrittenBookContent, ItemCodecHelper::writeWrittenBookContent, ObjectDataComponent::new);
    public static final DataComponentType<ArmorTrim> TRIM = new DataComponentType<>(ItemCodecHelper::readArmorTrim, ItemCodecHelper::writeArmorTrim, ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> DEBUG_STICK_STATE = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> BUCKET_ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> BLOCK_ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<Instrument> INSTRUMENT = new DataComponentType<>(ItemCodecHelper::readInstrument, ItemCodecHelper::writeInstrument, ObjectDataComponent::new);
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<CompoundTag> RECIPES = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = new DataComponentType<>(ItemCodecHelper::readLodestoneTarget, ItemCodecHelper::writeLodestoneTarget, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = new DataComponentType<>(ItemCodecHelper::readFireworkExplosion, ItemCodecHelper::writeFireworkExplosion, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks> FIREWORKS = new DataComponentType<>(ItemCodecHelper::readFireworks, ItemCodecHelper::writeFireworks, ObjectDataComponent::new);
    public static final DataComponentType<GameProfile> PROFILE = new DataComponentType<>(ItemCodecHelper::readResolvableProfile, ItemCodecHelper::writeResolvableProfile, ObjectDataComponent::new);
    public static final DataComponentType<String> NOTE_BLOCK_SOUND = new DataComponentType<>(ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = new DataComponentType<>(listReader(ItemCodecHelper::readBannerPattern), listWriter(ItemCodecHelper::writeBannerPattern), ObjectDataComponent::new);
    public static final IntComponentType BASE_COLOR = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = new DataComponentType<>(listReader(ItemCodecHelper::readVarInt), listWriter(ItemCodecHelper::writeVarInt), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CONTAINER = new DataComponentType<>(listReader(ItemCodecHelper::readOptionalItemStack), listWriter(MinecraftCodecHelper::writeOptionalItemStack), ObjectDataComponent::new);
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = new DataComponentType<>(ItemCodecHelper::readBlockStateProperties, ItemCodecHelper::writeBlockStateProperties, ObjectDataComponent::new);
    public static final DataComponentType<List<BeehiveOccupant>> BEES = new DataComponentType<>(listReader(ItemCodecHelper::readBeehiveOccupant), listWriter(ItemCodecHelper::writeBeehiveOccupant), ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> LOCK = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<CompoundTag> CONTAINER_LOOT = new DataComponentType<>(ItemCodecHelper::readAnyTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);

    protected final int id;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final DataComponentFactory<T> dataComponentFactory;

    protected DataComponentType(Reader<T> reader, Writer<T> writer, DataComponentFactory<T> dataComponentFactory) {
        this.id = VALUES.size();
        this.reader = reader;
        this.writer = writer;
        this.dataComponentFactory = dataComponentFactory;

        VALUES.add(this);
    }

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ItemCodecHelper helper, ByteBuf input) throws IOException {
        return this.dataComponentFactory.create(this, this.reader.read(helper, input));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ItemCodecHelper helper, ByteBuf output, T value) throws IOException {
        this.writer.write(helper, output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ItemCodecHelper helper, ByteBuf input) throws IOException;
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ItemCodecHelper helper, ByteBuf output, V value) throws IOException;
    }

    @FunctionalInterface
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input) throws IOException;

        default V read(ItemCodecHelper helper, ByteBuf input) throws IOException {
            return this.read(input);
        }
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V Value) throws IOException;

        default void write(ItemCodecHelper helper, ByteBuf output, V value) throws IOException {
            this.write(output, value);
        }
    }

    @FunctionalInterface
    public interface DataComponentFactory<V> {
        DataComponent<V, ? extends DataComponentType<V>> create(DataComponentType<V> type, V value);
    }

    private static <T> Reader<List<T>> listReader(Reader<T> reader) {
        return (helper, input) -> {
            List<T> ret = new ArrayList<>();
            for (int i = 0; i < helper.readVarInt(input); i++) {
                ret.add(reader.read(helper, input));
            }

            return ret;
        };
    }

    private static <T> Writer<List<T>> listWriter(Writer<T> writer) {
        return (helper, output, value) -> {
            helper.writeVarInt(output, value.size());
            for (T object : value) {
                writer.write(helper, output, object);
            }
        };
    }

    private static Reader<Void> unitReader() {
        return (helper, input) -> null;
    }

    private static Writer<Void> unitWriter() {
        return (helper, output, value) -> {};
    }

    public static DataComponentType<?> read(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        int id = helper.readVarInt(in);
        if (id >= VALUES.size()) {
            throw new IllegalArgumentException("Received id " + id + " for DataComponentType when the maximum was " + VALUES.size() + "!");
        }

        return VALUES.get(id);
    }

    public static DataComponentType<?> from(int id) {
        return VALUES.get(id);
    }

    public static int size() {
        return VALUES.size();
    }
}
