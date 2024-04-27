package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import com.github.steveice10.mc.auth.data.GameProfile;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.BooleanDataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.IntDataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.ObjectDataComponent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataComponentTypes {
    private static final List<DataComponentType<?>> VALUES = new ArrayList<>();

    public static final DataComponentType<NbtMap> CUSTOM_DATA = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final IntComponentType MAX_STACK_SIZE = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final IntComponentType MAX_DAMAGE = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final IntComponentType DAMAGE = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final BooleanComponentType UNBREAKABLE = register(new BooleanComponentType(nextId(), ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new));
    public static final DataComponentType<Component> CUSTOM_NAME = register(new DataComponentType<>(nextId(), ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new));
    public static final DataComponentType<Component> ITEM_NAME = register(new DataComponentType<>(nextId(), ItemCodecHelper::readComponent, ItemCodecHelper::writeComponent, ObjectDataComponent::new));
    public static final DataComponentType<List<Component>> LORE = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readComponent), listWriter(ItemCodecHelper::writeComponent), ObjectDataComponent::new));
    public static final IntComponentType RARITY = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new));
    public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = register(new DataComponentType<>(nextId(), ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new));
    public static final DataComponentType<AdventureModePredicate> CAN_BREAK = register(new DataComponentType<>(nextId(), ItemCodecHelper::readAdventureModePredicate, ItemCodecHelper::writeAdventureModePredicate, ObjectDataComponent::new));
    public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readItemAttributeModifiers, ItemCodecHelper::writeItemAttributeModifiers, ObjectDataComponent::new));
    public static final IntComponentType CUSTOM_MODEL_DATA = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<Unit> HIDE_ADDITIONAL_TOOLTIP = register(new DataComponentType<>(nextId(), unitReader(), unitWriter(), ObjectDataComponent::new));
    public static final DataComponentType<Unit> HIDE_TOOLTIP = register(new DataComponentType<>(nextId(), unitReader(), unitWriter(), ObjectDataComponent::new));
    public static final IntComponentType REPAIR_COST = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = register(new DataComponentType<>(nextId(), unitReader(), unitWriter(), ObjectDataComponent::new));
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = register(new BooleanComponentType(nextId(), ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new));
    public static final DataComponentType<NbtMap> INTANGIBLE_PROJECTILE = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final DataComponentType<FoodProperties> FOOD = register(new DataComponentType<>(nextId(), ItemCodecHelper::readFoodProperties, ItemCodecHelper::writeFoodProperties, ObjectDataComponent::new));
    public static final DataComponentType<Unit> FIRE_RESISTANT = register(new DataComponentType<>(nextId(), unitReader(), unitWriter(), ObjectDataComponent::new));
    public static final DataComponentType<ToolData> TOOL = register(new DataComponentType<>(nextId(), ItemCodecHelper::readToolData, ItemCodecHelper::writeToolData, ObjectDataComponent::new));
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new));
    public static final DataComponentType<DyedItemColor> DYED_COLOR = register(new DataComponentType<>(nextId(), ItemCodecHelper::readDyedItemColor, ItemCodecHelper::writeDyedItemColor, ObjectDataComponent::new));
    public static final IntComponentType MAP_COLOR = register(new IntComponentType(nextId(), (helper, input) -> input.readInt(), (helper, output, value) -> output.writeInt(value), IntDataComponent::new));
    public static final IntComponentType MAP_ID = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<NbtMap> MAP_DECORATIONS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final IntComponentType MAP_POST_PROCESSING = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new));
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new));
    public static final DataComponentType<PotionContents> POTION_CONTENTS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readPotionContents, ItemCodecHelper::writePotionContents, ObjectDataComponent::new));
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readStewEffect), listWriter(ItemCodecHelper::writeStewEffect), ObjectDataComponent::new));
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = register(new DataComponentType<>(nextId(), ItemCodecHelper::readWritableBookContent, ItemCodecHelper::writeWritableBookContent, ObjectDataComponent::new));
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = register(new DataComponentType<>(nextId(), ItemCodecHelper::readWrittenBookContent, ItemCodecHelper::writeWrittenBookContent, ObjectDataComponent::new));
    public static final DataComponentType<ArmorTrim> TRIM = register(new DataComponentType<>(nextId(), ItemCodecHelper::readArmorTrim, ItemCodecHelper::writeArmorTrim, ObjectDataComponent::new));
    public static final DataComponentType<NbtMap> DEBUG_STICK_STATE = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final DataComponentType<NbtMap> ENTITY_DATA = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final DataComponentType<NbtMap> BUCKET_ENTITY_DATA = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final DataComponentType<NbtMap> BLOCK_ENTITY_DATA = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));
    public static final DataComponentType<Holder<Instrument>> INSTRUMENT = register(new DataComponentType<>(nextId(), ItemCodecHelper::readInstrument, ItemCodecHelper::writeInstrument, ObjectDataComponent::new));
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<NbtList<?>> RECIPES = register(new DataComponentType<>(nextId(), ItemCodecHelper::readRecipes, ItemCodecHelper::writeRecipes, ObjectDataComponent::new));
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = register(new DataComponentType<>(nextId(), ItemCodecHelper::readLodestoneTarget, ItemCodecHelper::writeLodestoneTarget, ObjectDataComponent::new));
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = register(new DataComponentType<>(nextId(), ItemCodecHelper::readFireworkExplosion, ItemCodecHelper::writeFireworkExplosion, ObjectDataComponent::new));
    public static final DataComponentType<Fireworks> FIREWORKS = register(new DataComponentType<>(nextId(), ItemCodecHelper::readFireworks, ItemCodecHelper::writeFireworks, ObjectDataComponent::new));
    public static final DataComponentType<GameProfile> PROFILE = register(new DataComponentType<>(nextId(), ItemCodecHelper::readResolvableProfile, ItemCodecHelper::writeResolvableProfile, ObjectDataComponent::new));
    public static final DataComponentType<String> NOTE_BLOCK_SOUND = register(new DataComponentType<>(nextId(), ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new));
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readBannerPatternLayer), listWriter(ItemCodecHelper::writeBannerPatternLayer), ObjectDataComponent::new));
    public static final IntComponentType BASE_COLOR = register(new IntComponentType(nextId(), ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new));
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readVarInt), listWriter(ItemCodecHelper::writeVarInt), ObjectDataComponent::new));
    public static final DataComponentType<List<ItemStack>> CONTAINER = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readOptionalItemStack), listWriter(MinecraftCodecHelper::writeOptionalItemStack), ObjectDataComponent::new));
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = register(new DataComponentType<>(nextId(), ItemCodecHelper::readBlockStateProperties, ItemCodecHelper::writeBlockStateProperties, ObjectDataComponent::new));
    public static final DataComponentType<List<BeehiveOccupant>> BEES = register(new DataComponentType<>(nextId(), listReader(ItemCodecHelper::readBeehiveOccupant), listWriter(ItemCodecHelper::writeBeehiveOccupant), ObjectDataComponent::new));
    public static final DataComponentType<String> LOCK = register(new DataComponentType<>(nextId(), ItemCodecHelper::readLock, ItemCodecHelper::writeLock, ObjectDataComponent::new));
    public static final DataComponentType<NbtMap> CONTAINER_LOOT = register(new DataComponentType<>(nextId(), ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new));

    private DataComponentTypes() {
    }

    private static int nextId() {
        return VALUES.size();
    }

    private static <T extends DataComponentType<?>> T register(T type) {
        VALUES.add(type);
        return type;
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

    private static <T> DataComponentType.Reader<List<T>> listReader(DataComponentType.Reader<T> reader) {
        return (helper, input) -> {
            List<T> ret = new ArrayList<>();
            int size = helper.readVarInt(input);
            for (int i = 0; i < size; i++) {
                ret.add(reader.read(helper, input));
            }

            return ret;
        };
    }

    private static <T> DataComponentType.Writer<List<T>> listWriter(DataComponentType.Writer<T> writer) {
        return (helper, output, value) -> {
            helper.writeVarInt(output, value.size());
            for (T object : value) {
                writer.write(helper, output, object);
            }
        };
    }

    private static DataComponentType.Reader<Unit> unitReader() {
        return (helper, input) -> Unit.INSTANCE;
    }

    private static DataComponentType.Writer<Unit> unitWriter() {
        return (helper, output, value) -> {
        };
    }
}
