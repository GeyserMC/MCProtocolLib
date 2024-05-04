package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import com.github.steveice10.mc.auth.data.GameProfile;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.network.codec.DelegatedByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
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

    public static final DataComponentType<NbtMap> CUSTOM_DATA = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAX_STACK_SIZE = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final IntComponentType MAX_DAMAGE = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final IntComponentType DAMAGE = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final BooleanComponentType UNBREAKABLE = new BooleanComponentType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<Component> CUSTOM_NAME = new DataComponentType<>(ItemCodecByteBuf::readComponent, ItemCodecByteBuf::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<Component> ITEM_NAME = new DataComponentType<>(ItemCodecByteBuf::readComponent, ItemCodecByteBuf::writeComponent, ObjectDataComponent::new);
    public static final DataComponentType<List<Component>> LORE = new DataComponentType<>(listReader(ItemCodecByteBuf::readComponent), listWriter(ItemCodecByteBuf::writeComponent), ObjectDataComponent::new);
    public static final IntComponentType RARITY = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = new DataComponentType<>(ItemCodecByteBuf::readItemEnchantments, ItemCodecByteBuf::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = new DataComponentType<>(ItemCodecByteBuf::readAdventureModePredicate, ItemCodecByteBuf::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<AdventureModePredicate> CAN_BREAK = new DataComponentType<>(ItemCodecByteBuf::readAdventureModePredicate, ItemCodecByteBuf::writeAdventureModePredicate, ObjectDataComponent::new);
    public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = new DataComponentType<>(ItemCodecByteBuf::readItemAttributeModifiers, ItemCodecByteBuf::writeItemAttributeModifiers, ObjectDataComponent::new);
    public static final IntComponentType CUSTOM_MODEL_DATA = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unit> HIDE_ADDITIONAL_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final IntComponentType REPAIR_COST = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = new BooleanComponentType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<NbtMap> INTANGIBLE_PROJECTILE = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<FoodProperties> FOOD = new DataComponentType<>(ItemCodecByteBuf::readFoodProperties, ItemCodecByteBuf::writeFoodProperties, ObjectDataComponent::new);
    public static final DataComponentType<Unit> FIRE_RESISTANT = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<ToolData> TOOL = new DataComponentType<>(ItemCodecByteBuf::readToolData, ItemCodecByteBuf::writeToolData, ObjectDataComponent::new);
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentType<>(ItemCodecByteBuf::readItemEnchantments, ItemCodecByteBuf::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<DyedItemColor> DYED_COLOR = new DataComponentType<>(ItemCodecByteBuf::readDyedItemColor, ItemCodecByteBuf::writeDyedItemColor, ObjectDataComponent::new);
    public static final IntComponentType MAP_COLOR = new IntComponentType(ByteBuf::readInt, ByteBuf::writeInt, IntDataComponent::new);
    public static final IntComponentType MAP_ID = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtMap> MAP_DECORATIONS = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAP_POST_PROCESSING = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = new DataComponentType<>(listReader(ItemCodecByteBuf::readItemStack), listWriter(ItemCodecByteBuf::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = new DataComponentType<>(listReader(ItemCodecByteBuf::readItemStack), listWriter(ItemCodecByteBuf::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<PotionContents> POTION_CONTENTS = new DataComponentType<>(ItemCodecByteBuf::readPotionContents, ItemCodecByteBuf::writePotionContents, ObjectDataComponent::new);
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = new DataComponentType<>(listReader(ItemCodecByteBuf::readStewEffect), listWriter(ItemCodecByteBuf::writeStewEffect), ObjectDataComponent::new);
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = new DataComponentType<>(ItemCodecByteBuf::readWritableBookContent, ItemCodecByteBuf::writeWritableBookContent, ObjectDataComponent::new);
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = new DataComponentType<>(ItemCodecByteBuf::readWrittenBookContent, ItemCodecByteBuf::writeWrittenBookContent, ObjectDataComponent::new);
    public static final DataComponentType<ArmorTrim> TRIM = new DataComponentType<>(ItemCodecByteBuf::readArmorTrim, ItemCodecByteBuf::writeArmorTrim, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> DEBUG_STICK_STATE = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> ENTITY_DATA = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BUCKET_ENTITY_DATA = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BLOCK_ENTITY_DATA = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<Holder<Instrument>> INSTRUMENT = new DataComponentType<>(ItemCodecByteBuf::readInstrument, ItemCodecByteBuf::writeInstrument, ObjectDataComponent::new);
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtList<?>> RECIPES = new DataComponentType<>(ItemCodecByteBuf::readRecipes, ItemCodecByteBuf::writeRecipes, ObjectDataComponent::new);
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = new DataComponentType<>(ItemCodecByteBuf::readLodestoneTarget, ItemCodecByteBuf::writeLodestoneTarget, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = new DataComponentType<>(ItemCodecByteBuf::readFireworkExplosion, ItemCodecByteBuf::writeFireworkExplosion, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks> FIREWORKS = new DataComponentType<>(ItemCodecByteBuf::readFireworks, ItemCodecByteBuf::writeFireworks, ObjectDataComponent::new);
    public static final DataComponentType<GameProfile> PROFILE = new DataComponentType<>(ItemCodecByteBuf::readResolvableProfile, ItemCodecByteBuf::writeResolvableProfile, ObjectDataComponent::new);
    public static final DataComponentType<String> NOTE_BLOCK_SOUND = new DataComponentType<>(ItemCodecByteBuf::readResourceLocation, ItemCodecByteBuf::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = new DataComponentType<>(listReader(ItemCodecByteBuf::readBannerPatternLayer), listWriter(ItemCodecByteBuf::writeBannerPatternLayer), ObjectDataComponent::new);
    public static final IntComponentType BASE_COLOR = new IntComponentType(ItemCodecByteBuf::readVarInt, ItemCodecByteBuf::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = new DataComponentType<>(listReader(ItemCodecByteBuf::readVarInt), listWriter(ItemCodecByteBuf::writeVarInt), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CONTAINER = new DataComponentType<>(listReader(ItemCodecByteBuf::readOptionalItemStack), listWriter(MinecraftByteBuf::writeOptionalItemStack), ObjectDataComponent::new);
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = new DataComponentType<>(ItemCodecByteBuf::readBlockStateProperties, ItemCodecByteBuf::writeBlockStateProperties, ObjectDataComponent::new);
    public static final DataComponentType<List<BeehiveOccupant>> BEES = new DataComponentType<>(listReader(ItemCodecByteBuf::readBeehiveOccupant), listWriter(ItemCodecByteBuf::writeBeehiveOccupant), ObjectDataComponent::new);
    public static final DataComponentType<String> LOCK = new DataComponentType<>(ItemCodecByteBuf::readLock, ItemCodecByteBuf::writeLock, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> CONTAINER_LOOT = new DataComponentType<>(ItemCodecByteBuf::readCompoundTag, ItemCodecByteBuf::writeAnyTag, ObjectDataComponent::new);

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

    public DataComponent<T, ? extends DataComponentType<T>> readDataComponent(ItemCodecByteBuf helper) {
        return this.dataComponentFactory.create(this, this.reader.read(helper));
    }

    public DataComponent<T, ? extends DataComponentType<T>> readNullDataComponent() {
        return this.dataComponentFactory.create(this, null);
    }

    public void writeDataComponent(ItemCodecByteBuf helper, T value) {
        this.writer.write(helper, value);
    }

    @FunctionalInterface
    public interface Reader<V> {
        V read(ItemCodecByteBuf helper);
    }

    @FunctionalInterface
    public interface Writer<V> {
        void write(ItemCodecByteBuf helper, V value);
    }

    @FunctionalInterface
    public interface DataComponentFactory<V> {
        DataComponent<V, ? extends DataComponentType<V>> create(DataComponentType<V> type, V value);
    }

    private static <T> Reader<List<T>> listReader(Reader<T> reader) {
        return (input) -> {
            List<T> ret = new ArrayList<>();
            int size = input.readVarInt();
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(input));
            }

            return ret;
        };
    }

    private static <T> Writer<List<T>> listWriter(Writer<T> writer) {
        return (output, value) -> {
            output.writeVarInt(value.size());
            for (T object : value) {
                writer.write(output, object);
            }
        };
    }

    private static Reader<Unit> unitReader() {
        return input -> Unit.INSTANCE;
    }

    private static Writer<Unit> unitWriter() {
        return (output, value) -> {
        };
    }

    public static DataComponentType<?> read(MinecraftByteBuf buf) {
        int id = buf.readVarInt();
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
