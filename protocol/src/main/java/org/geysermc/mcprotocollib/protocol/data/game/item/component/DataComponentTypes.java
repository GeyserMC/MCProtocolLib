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
public class DataComponentTypes<T> {
    public static final DataComponentType<NbtMap> CUSTOM_DATA = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
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
    public static final DataComponentType<Unit> HIDE_ADDITIONAL_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<Unit> HIDE_TOOLTIP = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final IntComponentType REPAIR_COST = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final BooleanComponentType ENCHANTMENT_GLINT_OVERRIDE = new BooleanComponentType(ByteBuf::readBoolean, ByteBuf::writeBoolean, BooleanDataComponent::new);
    public static final DataComponentType<NbtMap> INTANGIBLE_PROJECTILE = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<FoodProperties> FOOD = new DataComponentType<>(ItemCodecHelper::readFoodProperties, ItemCodecHelper::writeFoodProperties, ObjectDataComponent::new);
    public static final DataComponentType<Unit> FIRE_RESISTANT = new DataComponentType<>(unitReader(), unitWriter(), ObjectDataComponent::new);
    public static final DataComponentType<ToolData> TOOL = new DataComponentType<>(ItemCodecHelper::readToolData, ItemCodecHelper::writeToolData, ObjectDataComponent::new);
    public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = new DataComponentType<>(ItemCodecHelper::readItemEnchantments, ItemCodecHelper::writeItemEnchantments, ObjectDataComponent::new);
    public static final DataComponentType<DyedItemColor> DYED_COLOR = new DataComponentType<>(ItemCodecHelper::readDyedItemColor, ItemCodecHelper::writeDyedItemColor, ObjectDataComponent::new);
    public static final IntComponentType MAP_COLOR = new IntComponentType((helper, input) -> input.readInt(), (helper, output, value) -> output.writeInt(value), IntDataComponent::new);
    public static final IntComponentType MAP_ID = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtMap> MAP_DECORATIONS = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final IntComponentType MAP_POST_PROCESSING = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CHARGED_PROJECTILES = new DataComponentType<>(listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> BUNDLE_CONTENTS = new DataComponentType<>(listReader(ItemCodecHelper::readItemStack), listWriter(ItemCodecHelper::writeItemStack), ObjectDataComponent::new);
    public static final DataComponentType<PotionContents> POTION_CONTENTS = new DataComponentType<>(ItemCodecHelper::readPotionContents, ItemCodecHelper::writePotionContents, ObjectDataComponent::new);
    public static final DataComponentType<List<SuspiciousStewEffect>> SUSPICIOUS_STEW_EFFECTS = new DataComponentType<>(listReader(ItemCodecHelper::readStewEffect), listWriter(ItemCodecHelper::writeStewEffect), ObjectDataComponent::new);
    public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = new DataComponentType<>(ItemCodecHelper::readWritableBookContent, ItemCodecHelper::writeWritableBookContent, ObjectDataComponent::new);
    public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = new DataComponentType<>(ItemCodecHelper::readWrittenBookContent, ItemCodecHelper::writeWrittenBookContent, ObjectDataComponent::new);
    public static final DataComponentType<ArmorTrim> TRIM = new DataComponentType<>(ItemCodecHelper::readArmorTrim, ItemCodecHelper::writeArmorTrim, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> DEBUG_STICK_STATE = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BUCKET_ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> BLOCK_ENTITY_DATA = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);
    public static final DataComponentType<Holder<Instrument>> INSTRUMENT = new DataComponentType<>(ItemCodecHelper::readInstrument, ItemCodecHelper::writeInstrument, ObjectDataComponent::new);
    public static final IntComponentType OMINOUS_BOTTLE_AMPLIFIER = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<NbtList<?>> RECIPES = new DataComponentType<>(ItemCodecHelper::readRecipes, ItemCodecHelper::writeRecipes, ObjectDataComponent::new);
    public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = new DataComponentType<>(ItemCodecHelper::readLodestoneTarget, ItemCodecHelper::writeLodestoneTarget, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks.FireworkExplosion> FIREWORK_EXPLOSION = new DataComponentType<>(ItemCodecHelper::readFireworkExplosion, ItemCodecHelper::writeFireworkExplosion, ObjectDataComponent::new);
    public static final DataComponentType<Fireworks> FIREWORKS = new DataComponentType<>(ItemCodecHelper::readFireworks, ItemCodecHelper::writeFireworks, ObjectDataComponent::new);
    public static final DataComponentType<GameProfile> PROFILE = new DataComponentType<>(ItemCodecHelper::readResolvableProfile, ItemCodecHelper::writeResolvableProfile, ObjectDataComponent::new);
    public static final DataComponentType<String> NOTE_BLOCK_SOUND = new DataComponentType<>(ItemCodecHelper::readResourceLocation, ItemCodecHelper::writeResourceLocation, ObjectDataComponent::new);
    public static final DataComponentType<List<BannerPatternLayer>> BANNER_PATTERNS = new DataComponentType<>(listReader(ItemCodecHelper::readBannerPatternLayer), listWriter(ItemCodecHelper::writeBannerPatternLayer), ObjectDataComponent::new);
    public static final IntComponentType BASE_COLOR = new IntComponentType(ItemCodecHelper::readVarInt, ItemCodecHelper::writeVarInt, IntDataComponent::new);
    public static final DataComponentType<List<Integer>> POT_DECORATIONS = new DataComponentType<>(listReader(ItemCodecHelper::readVarInt), listWriter(ItemCodecHelper::writeVarInt), ObjectDataComponent::new);
    public static final DataComponentType<List<ItemStack>> CONTAINER = new DataComponentType<>(listReader(ItemCodecHelper::readOptionalItemStack), listWriter(MinecraftCodecHelper::writeOptionalItemStack), ObjectDataComponent::new);
    public static final DataComponentType<BlockStateProperties> BLOCK_STATE = new DataComponentType<>(ItemCodecHelper::readBlockStateProperties, ItemCodecHelper::writeBlockStateProperties, ObjectDataComponent::new);
    public static final DataComponentType<List<BeehiveOccupant>> BEES = new DataComponentType<>(listReader(ItemCodecHelper::readBeehiveOccupant), listWriter(ItemCodecHelper::writeBeehiveOccupant), ObjectDataComponent::new);
    public static final DataComponentType<String> LOCK = new DataComponentType<>(ItemCodecHelper::readLock, ItemCodecHelper::writeLock, ObjectDataComponent::new);
    public static final DataComponentType<NbtMap> CONTAINER_LOOT = new DataComponentType<>(ItemCodecHelper::readCompoundTag, ItemCodecHelper::writeAnyTag, ObjectDataComponent::new);

    private DataComponentTypes() {
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
