package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.BooleanDataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.IntDataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.ObjectDataComponent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataComponentType<T> {
    private static final List<DataComponentType<?>> VALUES = new ArrayList<>();

    public static final DataComponentType<NbtMap> CUSTOM_DATA = new DataComponentType<>("custom_data", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAX_STACK_SIZE = new IntComponentType("max_stack_size", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final IntComponentType MAX_DAMAGE = new IntComponentType("max_damage", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final IntComponentType DAMAGE = new IntComponentType("damage", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final BooleanComponentType UNBREAKABLE = new BooleanComponentType("unbreakable", ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<Component> CUSTOM_NAME = new DataComponentType<>("custom_name", ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Component> ITEM_NAME = new DataComponentType<>("item_name", ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Key> ITEM_MODEL = new DataComponentType<>("item_model", ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<Component>> LORE = new DataComponentType<>("lore", listReader(ItemCodecHelper::readComponent), listWriter(ItemCodecHelper::writeComponent), ObjectDataComponent::new);
    public static final IntComponentType RARITY = new IntComponentType("rarity", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = new DataComponentType<>("enchantments", ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = new DataComponentType<>("can_place_on", ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_BREAK = new DataComponentType<>("can_break", ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = new DataComponentType<>("attribute_modifiers", ItemCodecHelper::readItemAttributeModifiers, ItemCodecHelper::writeItemAttributeModifiers, ObjectDataComponent::new);
    public static final DataComponentType<CustomModelData> CUSTOM_MODEL_DATA = new DataComponentType<>("custom_model_data", ItemCodecHelper::readCustomModelData, ItemCodecHelper::writeCustomModelData, ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_ADDITIONAL_TOOLTIP = new DataComponentType<>("hide_additional_tooltip", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_TOOLTIP = new DataComponentType<>("hide_tooltip", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final IntComponentType REPAIR_COST = new IntComponentType("repair_cost", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = new DataComponentType<>("creative_slot_lock", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = new BooleanComponentType("enchantment_glint_override", ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<NbtMap> INTANGIBLE_PROJECTILE = new DataComponentType<>("intangible_projectile", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<FoodProperties> FOOD = new DataComponentType<>("food", ItemCodecHelper::readFoodProperties, ItemCodecHelper::writeFoodProperties, ObjectDataComponent::new);
    public static final DataComponentType<Consumable> CONSUMABLE = new DataComponentType<>("consumable", ItemCodecHelper::readConsumable, ItemCodecHelper::writeConsumable, ObjectDataComponent::new);
    public static final DataComponentType<ItemStack> USE_REMAINDER = new DataComponentType<>("use_remainder", ItemCodecHelper::readItemStack, ItemCodecHelper::writeItemStack, ObjectDataComponent::new);
    public static final DataComponentType<UseCooldown> USE_COOLDOWN = new DataComponentType<>("use_cooldown", ItemCodecHelper::readUseCooldown, ItemCodecHelper::writeUseCooldown, ObjectDataComponent::new);
    public static final DataComponentType<Key> DAMAGE_RESISTANT = new DataComponentType<>("damage_resistant", ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<ToolData> TOOL = new DataComponentType<>("tool", ItemCodecHelper::readToolData, ItemCodecHelper::writeToolData, ObjectDataComponent::new);
    public static final IntComponentType ENCHANTABLE = new IntComponentType("enchantable", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Equippable> EQUIPPABLE = new DataComponentType<>("equippable", ItemCodecHelper::readEquippable, ItemCodecHelper::writeEquippable, ObjectDataComponent::new);
    public static final DataComponentType<HolderSet> REPAIRABLE = new DataComponentType<>("repairable", ItemCodecHelper::readHolderSet, ItemCodecHelper::writeHolderSet, ObjectDataComponent::new);
    public static final DataComponentType<Unit> GLIDER = new DataComponentType<>("glider", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Key> TOOLTIP_STYLE = new DataComponentType<>("tooltip_style", ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<ConsumeEffect>> DEATH_PROTECTION = new DataComponentType<>("death_protection", listReader(ItemCodecHelper::readConsumeEffect), listWriter(ItemCodecHelper::writeConsumeEffect), ObjectDataComponent::new);
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentType<>("stored_enchantments", ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<DyedItemColor> DYED_COLOR = new DataComponentType<>("dyed_color", ItemCodecHelper::readDyedItemColor, ItemCodecHelper::writeDyedItemColor, ObjectDataComponent::new);
    public static final IntComponentType MAP_COLOR = new IntComponentType("map_color", (helper, input) -> input.readInt(), (helper, output, value) -> output.writeInt(value), IntDataComponent::new);
    public static final IntComponentType MAP_ID = new IntComponentType("map_id", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtMap> MAP_DECORATIONS = new DataComponentType<>("map_decorations", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAP_POST_PROCESSING = new IntComponentType("map_post_processing", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = new DataComponentType<>("charged_projectiles", listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = new DataComponentType<>("bundle_contents", listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<PotionContents> POTION_CONTENTS = new DataComponentType<>("potion_contents", ItemCodecHelper::readPotionContents, ItemCodecHelper::writePotionContents, ObjectDataComponent::new);
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = new DataComponentType<>("suspicious_stew_effects", listReader(ItemCodecHelper::readStewEffect), listWriter(ItemCodecHelper::writeStewEffect), ObjectDataComponent::new);
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = new DataComponentType<>("writable_book_content", ItemCodecHelper::readWritableBookContent, ItemCodecHelper::writeWritableBookContent, ObjectDataComponent::new);
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = new DataComponentType<>("written_book_content", ItemCodecHelper::readWrittenBookContent, ItemCodecHelper::writeWrittenBookContent, ObjectDataComponent::new);
    public static final DataComponentType<ArmorTrim> TRIM = new DataComponentType<>("trim", ItemCodecHelper::readArmorTrim, ItemCodecHelper::writeArmorTrim, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> DEBUG_STICK_STATE = new DataComponentType<>("debug_stick_state", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> ENTITY_DATA = new DataComponentType<>("entity_data", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BUCKET_ENTITY_DATA = new DataComponentType<>("bucket_entity_data", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BLOCK_ENTITY_DATA = new DataComponentType<>("block_entity_data", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<Holder<Instrument>> INSTRUMENT = new DataComponentType<>("instrument", ItemCodecHelper::readInstrument, ItemCodecHelper::writeInstrument, ObjectDataComponent::new);
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = new IntComponentType("ominous_bottle_amplifier", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<JukeboxPlayable> JUKEBOX_PLAYABLE = new DataComponentType<>("jukebox_playable", ItemCodecHelper::readJukeboxPlayable, ItemCodecHelper::writeJukeboxPlayable, ObjectDataComponent::new);
    public static final DataComponentType<NbtList<?>> RECIPES = new DataComponentType<>("recipes", ItemCodecHelper::readRecipes, ItemCodecHelper::writeRecipes, ObjectDataComponent::new);
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = new DataComponentType<>("lodestone_tracker", ItemCodecHelper::readLodestoneTarget, ItemCodecHelper::writeLodestoneTarget, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = new DataComponentType<>("firework_explosion", ItemCodecHelper::readFireworkExplosion, ItemCodecHelper::writeFireworkExplosion, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks> FIREWORKS = new DataComponentType<>("fireworks", ItemCodecHelper::readFireworks, ItemCodecHelper::writeFireworks, ObjectDataComponent::new);
    public static final DataComponentType<GameProfile> PROFILE = new DataComponentType<>("profile", ItemCodecHelper::readResolvableProfile, ItemCodecHelper::writeResolvableProfile, ObjectDataComponent::new);
    public static final DataComponentType<Key> NOTE_BLOCK_SOUND = new DataComponentType<>("note_block_sound", ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = new DataComponentType<>("banner_patterns", listReader(ItemCodecHelper::readBannerPatternLayer), listWriter(ItemCodecHelper::writeBannerPatternLayer), ObjectDataComponent::new);
    public static final IntComponentType BASE_COLOR = new IntComponentType("base_color", ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = new DataComponentType<>("pot_decorations", listReader(ItemCodecHelper::readVarInt), listWriter(ItemCodecHelper::writeVarInt), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CONTAINER = new DataComponentType<>("container", listReader(ItemCodecHelper::readOptionalItemStack), listWriter(MinecraftCodecHelper::writeOptionalItemStack), ObjectDataComponent::new);
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = new DataComponentType<>("block_state", ItemCodecHelper::readBlockStateProperties, ItemCodecHelper::writeBlockStateProperties, ObjectDataComponent::new);
    public static final DataComponentType<List<BeehiveOccupant>> BEES = new DataComponentType<>("bees", listReader(ItemCodecHelper::readBeehiveOccupant), listWriter(ItemCodecHelper::writeBeehiveOccupant), ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> LOCK = new DataComponentType<>("lock", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> CONTAINER_LOOT = new DataComponentType<>("container_loot", ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);

    protected final int id;
    protected final Key key;
    protected final Reader<T> reader;
    protected final Writer<T> writer;
    protected final DataComponentFactory<T> dataComponentFactory;

    protected DataComponentType(String key, Reader<T> reader, Writer<T> writer, DataComponentFactory<T> dataComponentFactory) {
        this.id = VALUES.size();
        //noinspection PatternValidation
        this.key = Key.key(key);
        this.reader = reader;
        this.writer = writer;
        this.dataComponentFactory = dataComponentFactory;

        VALUES.add(this);
    }

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ItemCodecHelper helper, ByteBuf input) {
        return this.dataComponentFactory.create(this, this.reader.read(helper, input));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ItemCodecHelper helper, ByteBuf output, T value) {
        this.writer.write(helper, output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ItemCodecHelper helper, ByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ItemCodecHelper helper, ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input);

        default V read(ItemCodecHelper helper, ByteBuf input) {
            return this.read(input);
        }
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V Value);

        default void write(ItemCodecHelper helper, ByteBuf output, V value) {
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
            int size = helper.readVarInt(input);
            for (int i = 0; i < size; i++) {
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

    private static Reader<Unit> unitReader() {
        return (helper, input) -> Unit.INSTANCE;
    }

    private static Writer<Unit> unitWriter() {
        return (helper, output, value) -> {
        };
    }

    public static DataComponentType<?> read(ByteBuf in, MinecraftCodecHelper helper) {
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

    @Override
    public String toString() {
        return "DataComponentType(id=" + id + " , key=" + key.asString() + ")";
    }
}
