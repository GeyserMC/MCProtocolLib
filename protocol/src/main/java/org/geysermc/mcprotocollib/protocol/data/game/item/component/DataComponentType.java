package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public static final DataComponentType<NbtMap> CUSTOM_DATA = new DataComponentType<>("custom_data", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAX_STACK_SIZE = new IntComponentType("max_stack_size", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final IntComponentType MAX_DAMAGE = new IntComponentType("max_damage", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final IntComponentType DAMAGE = new IntComponentType("damage", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unbreakable> UNBREAKABLE = new DataComponentType<>("unbreakable", ItemTypes::readUnbreakable, ItemTypes::writeUnbreakable, ObjectDataComponent::new);
    public static final DataComponentType<Component> CUSTOM_NAME = new DataComponentType<>("custom_name", MinecraftTypes::readComponent, MinecraftTypes::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Component> ITEM_NAME = new DataComponentType<>("item_name", MinecraftTypes::readComponent, MinecraftTypes::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Key> ITEM_MODEL = new DataComponentType<>("item_model", MinecraftTypes::readResourceLocation, MinecraftTypes::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<Component>> LORE = new DataComponentType<>("lore", listReader(MinecraftTypes::readComponent), listWriter(MinecraftTypes::writeComponent), ObjectDataComponent::new);
    public static final IntComponentType RARITY = new IntComponentType("rarity", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = new DataComponentType<>("enchantments", ItemTypes::readItemEnchantments, ItemTypes::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = new DataComponentType<>("can_place_on", ItemTypes::readAdventureModePredicate, ItemTypes::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_BREAK = new DataComponentType<>("can_break", ItemTypes::readAdventureModePredicate, ItemTypes::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = new DataComponentType<>("attribute_modifiers", ItemTypes::readItemAttributeModifiers, ItemTypes::writeItemAttributeModifiers, ObjectDataComponent::new);
    public static final DataComponentType<CustomModelData> CUSTOM_MODEL_DATA = new DataComponentType<>("custom_model_data", ItemTypes::readCustomModelData, ItemTypes::writeCustomModelData, ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_ADDITIONAL_TOOLTIP = new DataComponentType<>("hide_additional_tooltip", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_TOOLTIP = new DataComponentType<>("hide_tooltip", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final IntComponentType REPAIR_COST = new IntComponentType("repair_cost", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = new DataComponentType<>("creative_slot_lock", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = new BooleanComponentType("enchantment_glint_override", ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<NbtMap> INTANGIBLE_PROJECTILE = new DataComponentType<>("intangible_projectile", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<FoodProperties> FOOD = new DataComponentType<>("food", ItemTypes::readFoodProperties, ItemTypes::writeFoodProperties, ObjectDataComponent::new);
    public static final DataComponentType<Consumable> CONSUMABLE = new DataComponentType<>("consumable", ItemTypes::readConsumable, ItemTypes::writeConsumable, ObjectDataComponent::new);
    public static final DataComponentType<ItemStack> USE_REMAINDER = new DataComponentType<>("use_remainder", MinecraftTypes::readItemStack, MinecraftTypes::writeItemStack, ObjectDataComponent::new);
    public static final DataComponentType<UseCooldown> USE_COOLDOWN = new DataComponentType<>("use_cooldown", ItemTypes::readUseCooldown, ItemTypes::writeUseCooldown, ObjectDataComponent::new);
    public static final DataComponentType<Key> DAMAGE_RESISTANT = new DataComponentType<>("damage_resistant", MinecraftTypes::readResourceLocation, MinecraftTypes::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<ToolData> TOOL = new DataComponentType<>("tool", ItemTypes::readToolData, ItemTypes::writeToolData, ObjectDataComponent::new);
    public static final IntComponentType ENCHANTABLE = new IntComponentType("enchantable", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Equippable> EQUIPPABLE = new DataComponentType<>("equippable", ItemTypes::readEquippable, ItemTypes::writeEquippable, ObjectDataComponent::new);
    public static final DataComponentType<HolderSet> REPAIRABLE = new DataComponentType<>("repairable", MinecraftTypes::readHolderSet, MinecraftTypes::writeHolderSet, ObjectDataComponent::new);
    public static final DataComponentType<Unit> GLIDER = new DataComponentType<>("glider", unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Key> TOOLTIP_STYLE = new DataComponentType<>("tooltip_style", MinecraftTypes::readResourceLocation, MinecraftTypes::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<ConsumeEffect>> DEATH_PROTECTION = new DataComponentType<>("death_protection", listReader(ItemTypes::readConsumeEffect), listWriter(ItemTypes::writeConsumeEffect), ObjectDataComponent::new);
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentType<>("stored_enchantments", ItemTypes::readItemEnchantments, ItemTypes::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<DyedItemColor> DYED_COLOR = new DataComponentType<>("dyed_color", ItemTypes::readDyedItemColor, ItemTypes::writeDyedItemColor, ObjectDataComponent::new);
    public static final IntComponentType MAP_COLOR = new IntComponentType("map_color", ByteBuf::readInt, ByteBuf::writeInt, IntDataComponent::new);
    public static final IntComponentType MAP_ID = new IntComponentType("map_id", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtMap> MAP_DECORATIONS = new DataComponentType<>("map_decorations", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAP_POST_PROCESSING = new IntComponentType("map_post_processing", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = new DataComponentType<>("charged_projectiles", listReader(MinecraftTypes::readItemStack), listWriter(MinecraftTypes::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = new DataComponentType<>("bundle_contents", listReader(MinecraftTypes::readItemStack), listWriter(MinecraftTypes::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<PotionContents> POTION_CONTENTS = new DataComponentType<>("potion_contents", ItemTypes::readPotionContents, ItemTypes::writePotionContents, ObjectDataComponent::new);
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = new DataComponentType<>("suspicious_stew_effects", listReader(ItemTypes::readStewEffect), listWriter(ItemTypes::writeStewEffect), ObjectDataComponent::new);
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = new DataComponentType<>("writable_book_content", ItemTypes::readWritableBookContent, ItemTypes::writeWritableBookContent, ObjectDataComponent::new);
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = new DataComponentType<>("written_book_content", ItemTypes::readWrittenBookContent, ItemTypes::writeWrittenBookContent, ObjectDataComponent::new);
    public static final DataComponentType<ArmorTrim> TRIM = new DataComponentType<>("trim", ItemTypes::readArmorTrim, ItemTypes::writeArmorTrim, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> DEBUG_STICK_STATE = new DataComponentType<>("debug_stick_state", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> ENTITY_DATA = new DataComponentType<>("entity_data", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BUCKET_ENTITY_DATA = new DataComponentType<>("bucket_entity_data", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BLOCK_ENTITY_DATA = new DataComponentType<>("block_entity_data", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<Holder<Instrument>> INSTRUMENT = new DataComponentType<>("instrument", ItemTypes::readInstrument, ItemTypes::writeInstrument, ObjectDataComponent::new);
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = new IntComponentType("ominous_bottle_amplifier", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<JukeboxPlayable> JUKEBOX_PLAYABLE = new DataComponentType<>("jukebox_playable", ItemTypes::readJukeboxPlayable, ItemTypes::writeJukeboxPlayable, ObjectDataComponent::new);
    public static final DataComponentType<NbtList<?>> RECIPES = new DataComponentType<>("recipes", ItemTypes::readRecipes, ItemTypes::writeRecipes, ObjectDataComponent::new);
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = new DataComponentType<>("lodestone_tracker", ItemTypes::readLodestoneTarget, ItemTypes::writeLodestoneTarget, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = new DataComponentType<>("firework_explosion", ItemTypes::readFireworkExplosion, ItemTypes::writeFireworkExplosion, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks> FIREWORKS = new DataComponentType<>("fireworks", ItemTypes::readFireworks, ItemTypes::writeFireworks, ObjectDataComponent::new);
    public static final DataComponentType<GameProfile> PROFILE = new DataComponentType<>("profile", ItemTypes::readResolvableProfile, ItemTypes::writeResolvableProfile, ObjectDataComponent::new);
    public static final DataComponentType<Key> NOTE_BLOCK_SOUND = new DataComponentType<>("note_block_sound", MinecraftTypes::readResourceLocation, MinecraftTypes::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = new DataComponentType<>("banner_patterns", listReader(ItemTypes::readBannerPatternLayer), listWriter(ItemTypes::writeBannerPatternLayer), ObjectDataComponent::new);
    public static final IntComponentType BASE_COLOR = new IntComponentType("base_color", MinecraftTypes::readVarInt, MinecraftTypes::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = new DataComponentType<>("pot_decorations", listReader(MinecraftTypes::readVarInt), listWriter(MinecraftTypes::writeVarInt), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CONTAINER = new DataComponentType<>("container", listReader(MinecraftTypes::readOptionalItemStack), listWriter(MinecraftTypes::writeOptionalItemStack), ObjectDataComponent::new);
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = new DataComponentType<>("block_state", ItemTypes::readBlockStateProperties, ItemTypes::writeBlockStateProperties, ObjectDataComponent::new);
    public static final DataComponentType<List<BeehiveOccupant>> BEES = new DataComponentType<>("bees", listReader(ItemTypes::readBeehiveOccupant), listWriter(ItemTypes::writeBeehiveOccupant), ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> LOCK = new DataComponentType<>("lock", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> CONTAINER_LOOT = new DataComponentType<>("container_loot", MinecraftTypes::readCompoundTag, MinecraftTypes::writeAnyTag, ObjectDataComponent::new);

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

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ByteBuf input) {
        return this.dataComponentFactory.create(this, this.reader.read(input));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ByteBuf output, T value) {
        this.writer.write(output, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ByteBuf output, V value);
    }

    @FunctionalInterface
    public interface BasicReader<V> extends Reader<V> {
        V read(ByteBuf input);
    }

    @FunctionalInterface
    public interface BasicWriter<V> extends Writer<V> {
        void write(ByteBuf output, V Value);
    }

    @FunctionalInterface
    public interface DataComponentFactory<V> {
        DataComponent<V, ? extends DataComponentType<V>> create(DataComponentType<V> type, V value);
    }

    private static <T> Reader<List<T>> listReader(Reader<T> reader) {
        return (input) -> {
            List<T> ret = new ArrayList<>();
            int size = MinecraftTypes.readVarInt(input);
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(input));
            }

            return ret;
        };
    }

    private static <T> Writer<List<T>> listWriter(Writer<T> writer) {
        return (output, value) -> {
            MinecraftTypes.writeVarInt(output, value.size());
            for (T object : value) {
                writer.write(output, object);
            }
        };
    }

    private static Reader<Unit> unitReader() {
        return (input) -> Unit.INSTANCE;
    }

    private static Writer<Unit> unitWriter() {
        return (output, value) -> {
        };
    }

    public static DataComponentType<?> read(ByteBuf in) {
        int id = MinecraftTypes.readVarInt(in);
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
