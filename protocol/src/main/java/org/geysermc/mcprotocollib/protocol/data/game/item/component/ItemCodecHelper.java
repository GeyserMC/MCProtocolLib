package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class ItemCodecHelper extends MinecraftCodecHelper {
    public static ItemCodecHelper INSTANCE = new ItemCodecHelper();

    public ItemCodecHelper() {
        super(Int2ObjectMaps.emptyMap(), Collections.emptyMap());
    }

    public <T> Filterable<T> readFilterable(ByteBuf buf, Function<ByteBuf, T> reader) {
        T raw = reader.apply(buf);
        T filtered = null;
        if (buf.readBoolean()) {
            filtered = reader.apply(buf);
        }
        return new Filterable<>(raw, filtered);
    }

    public <T> void writeFilterable(ByteBuf buf, Filterable<T> filterable, BiConsumer<ByteBuf, T> writer) {
        writer.accept(buf, filterable.getRaw());
        if (filterable.getOptional() != null) {
            buf.writeBoolean(true);
            writer.accept(buf, filterable.getOptional());
        } else {
            buf.writeBoolean(false);
        }
    }

    public ItemEnchantments readItemEnchantments(ByteBuf buf) {
        Map<Integer, Integer> enchantments = new HashMap<>();
        int enchantmentCount = this.readVarInt(buf);
        for (int i = 0; i < enchantmentCount; i++) {
            enchantments.put(this.readVarInt(buf), this.readVarInt(buf));
        }

        return new ItemEnchantments(enchantments, buf.readBoolean());
    }

    public void writeItemEnchantments(ByteBuf buf, ItemEnchantments itemEnchantments) {
        this.writeVarInt(buf, itemEnchantments.getEnchantments().size());
        for (Map.Entry<Integer, Integer> entry : itemEnchantments.getEnchantments().entrySet()) {
            this.writeVarInt(buf, entry.getKey());
            this.writeVarInt(buf, entry.getValue());
        }

        buf.writeBoolean(itemEnchantments.isShowInTooltip());
    }

    public AdventureModePredicate readAdventureModePredicate(ByteBuf buf) {
        List<AdventureModePredicate.BlockPredicate> predicates = new ArrayList<>();
        int predicateCount = this.readVarInt(buf);
        for (int i = 0; i < predicateCount; i++) {
            predicates.add(this.readBlockPredicate(buf));
        }

        return new AdventureModePredicate(predicates, buf.readBoolean());
    }

    public void writeAdventureModePredicate(ByteBuf buf, AdventureModePredicate adventureModePredicate) {
        this.writeVarInt(buf, adventureModePredicate.getPredicates().size());
        for (AdventureModePredicate.BlockPredicate predicate : adventureModePredicate.getPredicates()) {
            this.writeBlockPredicate(buf, predicate);
        }

        buf.writeBoolean(adventureModePredicate.isShowInTooltip());
    }

    public AdventureModePredicate.BlockPredicate readBlockPredicate(ByteBuf buf) {
        String location = null;
        int[] holders = null;
        List<AdventureModePredicate.PropertyMatcher> propertyMatchers = null;

        if (buf.readBoolean()) {
            int length = this.readVarInt(buf) - 1;
            if (length == -1) {
                location = this.readResourceLocation(buf);
            } else {
                holders = new int[length];
                for (int i = 0; i < length; i++) {
                    holders[i] = this.readVarInt(buf);
                }
            }
        }

        if (buf.readBoolean()) {
            propertyMatchers = new ArrayList<>();
            int matcherCount = this.readVarInt(buf);
            for (int i = 0; i < matcherCount; i++) {
                String name = this.readString(buf);
                if (buf.readBoolean()) {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, this.readString(buf), null, null));
                } else {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, null, this.readString(buf), this.readString(buf)));
                }
            }
        }

        return new AdventureModePredicate.BlockPredicate(location, holders, propertyMatchers, this.readNullable(buf, this::readCompoundTag));
    }

    public void writeBlockPredicate(ByteBuf buf, AdventureModePredicate.BlockPredicate blockPredicate) {
        if (blockPredicate.getLocation() == null && blockPredicate.getHolders() == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            if (blockPredicate.getLocation() != null) {
                this.writeVarInt(buf, 0);
                this.writeResourceLocation(buf, blockPredicate.getLocation());
            } else {
                this.writeVarInt(buf, blockPredicate.getHolders().length + 1);
                for (int holder : blockPredicate.getHolders()) {
                    this.writeVarInt(buf, holder);
                }
            }
        }

        if (blockPredicate.getProperties() == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            for (AdventureModePredicate.PropertyMatcher matcher : blockPredicate.getProperties()) {
                this.writeString(buf, matcher.getName());
                if (matcher.getValue() != null) {
                    buf.writeBoolean(true);
                    this.writeString(buf, matcher.getValue());
                } else {
                    buf.writeBoolean(false);
                    this.writeString(buf, matcher.getMinValue());
                    this.writeString(buf, matcher.getMaxValue());
                }
            }
        }

        this.writeNullable(buf, blockPredicate.getNbt(), this::writeAnyTag);
    }

    public ToolData readToolData(ByteBuf buf) {
        List<ToolData.Rule> rules = new ArrayList<>();
        int ruleCount = this.readVarInt(buf);
        for (int i = 0; i < ruleCount; i++) {
            String location = null;
            int[] holders = null;

            int length = this.readVarInt(buf) - 1;
            if (length == -1) {
                location = this.readResourceLocation(buf);
            } else {
                holders = new int[length];
                for (int j = 0; j < length; j++) {
                    holders[j] = this.readVarInt(buf);
                }
            }

            Float speed = this.readNullable(buf, ByteBuf::readFloat);
            Boolean correctForDrops = this.readNullable(buf, ByteBuf::readBoolean);
            rules.add(new ToolData.Rule(location, holders, speed, correctForDrops));
        }

        float defaultMiningSpeed = buf.readFloat();
        int damagePerBlock = this.readVarInt(buf);
        return new ToolData(rules, defaultMiningSpeed, damagePerBlock);
    }

    public void writeToolData(ByteBuf buf, ToolData data) {
        this.writeVarInt(buf, data.getRules().size());
        for (ToolData.Rule rule : data.getRules()) {
            if (rule.getLocation() != null) {
                this.writeVarInt(buf, 0);
                this.writeResourceLocation(buf, rule.getLocation());
            } else {
                this.writeVarInt(buf, rule.getHolders().length + 1);
                for (int holder : rule.getHolders()) {
                    this.writeVarInt(buf, holder);
                }
            }

            this.writeNullable(buf, rule.getSpeed(), ByteBuf::writeFloat);
            this.writeNullable(buf, rule.getCorrectForDrops(), ByteBuf::writeBoolean);
        }

        buf.writeFloat(data.getDefaultMiningSpeed());
        this.writeVarInt(buf, data.getDamagePerBlock());
    }

    public ItemAttributeModifiers readItemAttributeModifiers(ByteBuf buf) {
        List<ItemAttributeModifiers.Entry> modifiers = new ArrayList<>();
        int modifierCount = this.readVarInt(buf);
        for (int i = 0; i < modifierCount; i++) {
            int attribute = this.readVarInt(buf);

            UUID id = this.readUUID(buf);
            String name = this.readString(buf);
            double amount = buf.readDouble();
            ModifierOperation operation = ModifierOperation.from(this.readVarInt(buf));
            ItemAttributeModifiers.AttributeModifier modifier = new ItemAttributeModifiers.AttributeModifier(id, name, amount, operation);

            ItemAttributeModifiers.EquipmentSlotGroup slot = ItemAttributeModifiers.EquipmentSlotGroup.from(this.readVarInt(buf));
            modifiers.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
        }

        return new ItemAttributeModifiers(modifiers, buf.readBoolean());
    }

    public void writeItemAttributeModifiers(ByteBuf buf, ItemAttributeModifiers modifiers) {
        this.writeVarInt(buf, modifiers.getModifiers().size());
        for (ItemAttributeModifiers.Entry modifier : modifiers.getModifiers()) {
            this.writeVarInt(buf, modifier.getAttribute());

            this.writeUUID(buf, modifier.getModifier().getId());
            this.writeString(buf, modifier.getModifier().getName());
            buf.writeDouble(modifier.getModifier().getAmount());
            this.writeVarInt(buf, modifier.getModifier().getOperation().ordinal());

            this.writeVarInt(buf, modifier.getSlot().ordinal());
        }

        buf.writeBoolean(modifiers.isShowInTooltip());
    }

    public DyedItemColor readDyedItemColor(ByteBuf buf) {
        return new DyedItemColor(buf.readInt(), buf.readBoolean());
    }

    public void writeDyedItemColor(ByteBuf buf, DyedItemColor itemColor) {
        buf.writeInt(itemColor.getRgb());
        buf.writeBoolean(itemColor.isShowInTooltip());
    }

    public PotionContents readPotionContents(ByteBuf buf) {
        int potionId = buf.readBoolean() ? this.readVarInt(buf) : -1;
        int customColor = buf.readBoolean() ? buf.readInt() : -1;

        List<MobEffectDetails> customEffects = new ArrayList<>();
        int effectCount = this.readVarInt(buf);
        for (int i = 0; i < effectCount; i++) {
            customEffects.add(this.readEffectDetails(buf));
        }
        return new PotionContents(potionId, customColor, customEffects);
    }

    public void writePotionContents(ByteBuf buf, PotionContents contents) {
        if (contents.getPotionId() < 0) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            this.writeVarInt(buf, contents.getPotionId());
        }

        if (contents.getCustomColor() < 0) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(contents.getCustomColor());
        }

        this.writeVarInt(buf, contents.getCustomEffects().size());
        for (MobEffectDetails customEffect : contents.getCustomEffects()) {
            this.writeEffectDetails(buf, customEffect);
        }
    }

    public FoodProperties readFoodProperties(ByteBuf buf) {
        int nutrition = this.readVarInt(buf);
        float saturationModifier = buf.readFloat();
        boolean canAlwaysEat = buf.readBoolean();
        float eatSeconds = buf.readFloat();

        List<FoodProperties.PossibleEffect> effects = new ArrayList<>();
        int effectCount = this.readVarInt(buf);
        for (int i = 0; i < effectCount; i++) {
            effects.add(new FoodProperties.PossibleEffect(this.readEffectDetails(buf), buf.readFloat()));
        }

        return new FoodProperties(nutrition, saturationModifier, canAlwaysEat, eatSeconds, effects);
    }

    public void writeFoodProperties(ByteBuf buf, FoodProperties properties) {
        this.writeVarInt(buf, properties.getNutrition());
        buf.writeFloat(properties.getSaturationModifier());
        buf.writeBoolean(properties.isCanAlwaysEat());
        buf.writeFloat(properties.getEatSeconds());

        this.writeVarInt(buf, properties.getEffects().size());
        for (FoodProperties.PossibleEffect effect : properties.getEffects()) {
            this.writeEffectDetails(buf, effect.getEffect());
            buf.writeFloat(effect.getProbability());
        }
    }

    public MobEffectDetails readEffectDetails(ByteBuf buf) {
        Effect effect = this.readEffect(buf);
        int amplifier = this.readVarInt(buf);
        int duration = this.readVarInt(buf);
        boolean ambient = buf.readBoolean();
        boolean showParticles = buf.readBoolean();
        boolean showIcon = buf.readBoolean();
        MobEffectDetails hiddenEffect = this.readNullable(buf, this::readEffectDetails);
        return new MobEffectDetails(effect, amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
    }

    public void writeEffectDetails(ByteBuf buf, MobEffectDetails details) {
        this.writeEffect(buf, details.getEffect());
        this.writeVarInt(buf, details.getAmplifier());
        this.writeVarInt(buf, details.getDuration());
        buf.writeBoolean(details.isAmbient());
        buf.writeBoolean(details.isShowParticles());
        buf.writeBoolean(details.isShowIcon());
        this.writeNullable(buf, details.getHiddenEffect(), this::writeEffectDetails);
    }

    public SuspiciousStewEffect readStewEffect(ByteBuf buf) {
        return new SuspiciousStewEffect(this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeStewEffect(ByteBuf buf, SuspiciousStewEffect effect) {
        this.writeVarInt(buf, effect.getMobEffectId());
        this.writeVarInt(buf, effect.getDuration());
    }

    public WritableBookContent readWritableBookContent(ByteBuf buf) {
        List<Filterable<String>> pages = new ArrayList<>();
        int pageCount = this.readVarInt(buf);
        for (int i = 0; i < pageCount; i++) {
            pages.add(this.readFilterable(buf, this::readString));
        }

        return new WritableBookContent(pages);
    }

    public void writeWritableBookContent(ByteBuf buf, WritableBookContent content) {
        this.writeVarInt(buf, content.getPages().size());
        for (Filterable<String> page : content.getPages()) {
            this.writeFilterable(buf, page, this::writeString);
        }
    }

    public WrittenBookContent readWrittenBookContent(ByteBuf buf) {
        Filterable<String> title = this.readFilterable(buf, this::readString);
        String author = this.readString(buf);
        int generation = this.readVarInt(buf);

        List<Filterable<Component>> pages = new ArrayList<>();
        int pageCount = this.readVarInt(buf);
        for (int i = 0; i < pageCount; i++) {
            pages.add(this.readFilterable(buf, this::readComponent));
        }

        boolean resolved = buf.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public void writeWrittenBookContent(ByteBuf buf, WrittenBookContent content) {
        this.writeFilterable(buf, content.getTitle(), this::writeString);
        this.writeString(buf, content.getAuthor());
        this.writeVarInt(buf, content.getGeneration());

        this.writeVarInt(buf, content.getPages().size());
        for (Filterable<Component> page : content.getPages()) {
            this.writeFilterable(buf, page, this::writeComponent);
        }

        buf.writeBoolean(content.isResolved());
    }

    public ArmorTrim readArmorTrim(ByteBuf buf) {
        Holder<ArmorTrim.TrimMaterial> material = this.readHolder(buf, this::readTrimMaterial);
        Holder<ArmorTrim.TrimPattern> pattern = this.readHolder(buf, this::readTrimPattern);
        boolean showInTooltip = buf.readBoolean();
        return new ArmorTrim(material, pattern, showInTooltip);
    }

    public void writeArmorTrim(ByteBuf buf, ArmorTrim trim) {
        this.writeHolder(buf, trim.material(), this::writeTrimMaterial);
        this.writeHolder(buf, trim.pattern(), this::writeTrimPattern);
        buf.writeBoolean(trim.showInTooltip());
    }

    public ArmorTrim.TrimMaterial readTrimMaterial(ByteBuf buf) {
        String assetName = this.readString(buf);
        int ingredientId = this.readVarInt(buf);
        float itemModelIndex = buf.readFloat();

        Int2ObjectMap<String> overrideArmorMaterials = new Int2ObjectOpenHashMap<>();
        int overrideCount = this.readVarInt(buf);
        for (int i = 0; i < overrideCount; i++) {
            overrideArmorMaterials.put(this.readVarInt(buf), this.readString(buf));
        }

        Component description = this.readComponent(buf);
        return new ArmorTrim.TrimMaterial(assetName, ingredientId, itemModelIndex, overrideArmorMaterials, description);
    }

    public void writeTrimMaterial(ByteBuf buf, ArmorTrim.TrimMaterial material) {
        this.writeString(buf, material.assetName());
        this.writeVarInt(buf, material.ingredientId());
        buf.writeFloat(material.itemModelIndex());

        this.writeVarInt(buf, material.overrideArmorMaterials().size());
        for (Int2ObjectMap.Entry<String> entry : material.overrideArmorMaterials().int2ObjectEntrySet()) {
            this.writeVarInt(buf, entry.getIntKey());
            this.writeString(buf, entry.getValue());
        }

        this.writeComponent(buf, material.description());
    }

    public ArmorTrim.TrimPattern readTrimPattern(ByteBuf buf) {
        String assetId = this.readResourceLocation(buf);
        int templateItemId = this.readVarInt(buf);
        Component description = this.readComponent(buf);
        boolean decal = buf.readBoolean();
        return new ArmorTrim.TrimPattern(assetId, templateItemId, description, decal);
    }

    public void writeTrimPattern(ByteBuf buf, ArmorTrim.TrimPattern pattern) {
        this.writeResourceLocation(buf, pattern.assetId());
        this.writeVarInt(buf, pattern.templateItemId());
        this.writeComponent(buf, pattern.description());
        buf.writeBoolean(pattern.decal());
    }

    public Holder<Instrument> readInstrument(ByteBuf buf) {
        return this.readHolder(buf, (input) -> {
            Sound soundEvent = this.readById(input, BuiltinSound::from, this::readSoundEvent);
            int useDuration = this.readVarInt(input);
            float range = input.readFloat();
            return new Instrument(soundEvent, useDuration, range);
        });
    }

    public void writeInstrument(ByteBuf buf, Holder<Instrument> instrumentHolder) {
        this.writeHolder(buf, instrumentHolder, (output, instrument) -> {
            if (instrument.getSoundEvent() instanceof CustomSound) {
                this.writeVarInt(buf, 0);
                this.writeSoundEvent(buf, instrument.getSoundEvent());
            } else {
                this.writeVarInt(buf, ((BuiltinSound) instrument.getSoundEvent()).ordinal() + 1);
            }

            this.writeVarInt(buf, instrument.getUseDuration());
            buf.writeFloat(instrument.getRange());
        });
    }

    public NbtList<?> readRecipes(ByteBuf buf) {
        return this.readAnyTag(buf, NbtType.LIST);
    }

    public void writeRecipes(ByteBuf buf, NbtList<?> recipes) {
        this.writeAnyTag(buf, recipes);
    }

    public LodestoneTracker readLodestoneTarget(ByteBuf buf) {
        return new LodestoneTracker(this.readNullable(buf, this::readGlobalPos), buf.readBoolean());
    }

    public void writeLodestoneTarget(ByteBuf buf, LodestoneTracker target) {
        this.writeNullable(buf, target.getPos(), this::writeGlobalPos);
        buf.writeBoolean(target.isTracked());
    }

    public Fireworks readFireworks(ByteBuf buf) {
        int flightDuration = this.readVarInt(buf);

        List<Fireworks.FireworkExplosion> explosions = new ArrayList<>();
        int explosionCount = this.readVarInt(buf);
        for (int i = 0; i < explosionCount; i++) {
            explosions.add(this.readFireworkExplosion(buf));
        }

        return new Fireworks(flightDuration, explosions);
    }

    public void writeFireworks(ByteBuf buf, Fireworks fireworks) {
        this.writeVarInt(buf, fireworks.getFlightDuration());

        this.writeVarInt(buf, fireworks.getExplosions().size());
        for (Fireworks.FireworkExplosion explosion : fireworks.getExplosions()) {
            this.writeFireworkExplosion(buf, explosion);
        }
    }

    public Fireworks.FireworkExplosion readFireworkExplosion(ByteBuf buf) {
        int shapeId = this.readVarInt(buf);

        int[] colors = new int[this.readVarInt(buf)];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = buf.readInt();
        }

        int[] fadeColors = new int[this.readVarInt(buf)];
        for (int i = 0; i < fadeColors.length; i++) {
            fadeColors[i] = buf.readInt();
        }

        boolean hasTrail = buf.readBoolean();
        boolean hasTwinkle = buf.readBoolean();
        return new Fireworks.FireworkExplosion(shapeId, colors, fadeColors, hasTrail, hasTwinkle);
    }

    public void writeFireworkExplosion(ByteBuf buf, Fireworks.FireworkExplosion explosion) {
        this.writeVarInt(buf, explosion.getShapeId());

        this.writeVarInt(buf, explosion.getColors().length);
        for (int color : explosion.getColors()) {
            buf.writeInt(color);
        }

        this.writeVarInt(buf, explosion.getFadeColors().length);
        for (int fadeColor : explosion.getFadeColors()) {
            buf.writeInt(fadeColor);
        }

        buf.writeBoolean(explosion.isHasTrail());
        buf.writeBoolean(explosion.isHasTwinkle());
    }

    public GameProfile readResolvableProfile(ByteBuf buf) {
        String name = this.readNullable(buf, this::readString);
        UUID id = this.readNullable(buf, this::readUUID);
        GameProfile profile = new GameProfile(id, name);

        List<GameProfile.Property> properties = new ArrayList<>();
        int propertyCount = this.readVarInt(buf);
        for (int i = 0; i < propertyCount; i++) {
            properties.add(this.readProperty(buf));
        }
        profile.setProperties(properties);

        return profile;
    }

    public void writeResolvableProfile(ByteBuf buf, GameProfile profile) {
        this.writeNullable(buf, profile.getName(), this::writeString);
        this.writeNullable(buf, profile.getId(), this::writeUUID);

        this.writeVarInt(buf, profile.getProperties().size());
        for (GameProfile.Property property : profile.getProperties()) {
            this.writeProperty(buf, property);
        }
    }

    public BannerPatternLayer readBannerPatternLayer(ByteBuf buf) {
        return new BannerPatternLayer(this.readHolder(buf, this::readBannerPattern), this.readVarInt(buf));
    }

    public void writeBannerPatternLayer(ByteBuf buf, BannerPatternLayer patternLayer) {
        this.writeHolder(buf, patternLayer.getPattern(), this::writeBannerPattern);
        this.writeVarInt(buf, patternLayer.getColorId());
    }

    public BannerPatternLayer.BannerPattern readBannerPattern(ByteBuf buf) {
        return new BannerPatternLayer.BannerPattern(this.readResourceLocation(buf), this.readString(buf));
    }

    public void writeBannerPattern(ByteBuf buf, BannerPatternLayer.BannerPattern pattern) {
        this.writeResourceLocation(buf, pattern.getAssetId());
        this.writeString(buf, pattern.getTranslationKey());
    }

    public BlockStateProperties readBlockStateProperties(ByteBuf buf) {
        Map<String, String> properties = new HashMap<>();
        int propertyCount = this.readVarInt(buf);
        for (int i = 0; i < propertyCount; i++) {
            properties.put(this.readString(buf), this.readString(buf));
        }

        return new BlockStateProperties(properties);
    }

    public void writeBlockStateProperties(ByteBuf buf, BlockStateProperties props) {
        this.writeVarInt(buf, props.getProperties().size());
        for (Map.Entry<String, String> prop : props.getProperties().entrySet()) {
            this.writeString(buf, prop.getKey());
            this.writeString(buf, prop.getValue());
        }
    }

    public BeehiveOccupant readBeehiveOccupant(ByteBuf buf) {
        return new BeehiveOccupant(this.readCompoundTag(buf), this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeBeehiveOccupant(ByteBuf buf, BeehiveOccupant occupant) {
        this.writeAnyTag(buf, occupant.getEntityData());
        this.writeVarInt(buf, occupant.getTicksInHive());
        this.writeVarInt(buf, occupant.getMinTicksInHive());
    }

    public String readLock(ByteBuf buf) {
        return this.readAnyTag(buf, NbtType.STRING);
    }

    public void writeLock(ByteBuf buf, String key) {
        this.writeAnyTag(buf, key);
    }
}
