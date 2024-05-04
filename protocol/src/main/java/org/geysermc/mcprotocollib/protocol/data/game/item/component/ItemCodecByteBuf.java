package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import com.github.steveice10.mc.auth.data.GameProfile;
import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class ItemCodecByteBuf extends MinecraftByteBuf {
    public ItemCodecByteBuf(ByteBuf buf) {
        super(Int2ObjectMaps.emptyMap(), Collections.emptyMap(), buf);
    }

    public <T> Filterable<T> readFilterable(Supplier<T> reader) {
        T raw = reader.get();
        T filtered = null;
        if (this.readBoolean()) {
            filtered = reader.get();
        }
        return new Filterable<>(raw, filtered);
    }

    public <T> void writeFilterable(Filterable<T> filterable, Consumer<T> writer) {
        writer.accept(filterable.getRaw());
        if (filterable.getOptional() != null) {
            this.writeBoolean(true);
            writer.accept(filterable.getOptional());
        } else {
            this.writeBoolean(false);
        }
    }

    public ItemEnchantments readItemEnchantments() {
        Map<Integer, Integer> enchantments = new HashMap<>();
        int enchantmentCount = this.readVarInt();
        for (int i = 0; i < enchantmentCount; i++) {
            enchantments.put(this.readVarInt(), this.readVarInt());
        }

        return new ItemEnchantments(enchantments, this.readBoolean());
    }

    public void writeItemEnchantments(ItemEnchantments itemEnchantments) {
        this.writeVarInt(itemEnchantments.getEnchantments().size());
        for (Map.Entry<Integer, Integer> entry : itemEnchantments.getEnchantments().entrySet()) {
            this.writeVarInt(entry.getKey());
            this.writeVarInt(entry.getValue());
        }

        this.writeBoolean(itemEnchantments.isShowInTooltip());
    }

    public AdventureModePredicate readAdventureModePredicate() {
        List<AdventureModePredicate.BlockPredicate> predicates = new ArrayList<>();
        int predicateCount = this.readVarInt();
        for (int i = 0; i < predicateCount; i++) {
            predicates.add(this.readBlockPredicate());
        }

        return new AdventureModePredicate(predicates, this.readBoolean());
    }

    public void writeAdventureModePredicate(AdventureModePredicate adventureModePredicate) {
        this.writeVarInt(adventureModePredicate.getPredicates().size());
        for (AdventureModePredicate.BlockPredicate predicate : adventureModePredicate.getPredicates()) {
            this.writeBlockPredicate(predicate);
        }

        this.writeBoolean(adventureModePredicate.isShowInTooltip());
    }

    public AdventureModePredicate.BlockPredicate readBlockPredicate() {
        String location = null;
        int[] holders = null;
        List<AdventureModePredicate.PropertyMatcher> propertyMatchers = null;

        if (this.readBoolean()) {
            int length = this.readVarInt() - 1;
            if (length == -1) {
                location = this.readResourceLocation();
            } else {
                holders = new int[length];
                for (int i = 0; i < length; i++) {
                    holders[i] = this.readVarInt();
                }
            }
        }

        if (this.readBoolean()) {
            propertyMatchers = new ArrayList<>();
            int matcherCount = this.readVarInt();
            for (int i = 0; i < matcherCount; i++) {
                String name = this.readString();
                if (this.readBoolean()) {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, this.readString(), null, null));
                } else {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, null, this.readString(), this.readString()));
                }
            }
        }

        return new AdventureModePredicate.BlockPredicate(location, holders, propertyMatchers, this.readNullable(this::readCompoundTag));
    }

    public void writeBlockPredicate(AdventureModePredicate.BlockPredicate blockPredicate) {
        if (blockPredicate.getLocation() == null && blockPredicate.getHolders() == null) {
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            if (blockPredicate.getLocation() != null) {
                this.writeVarInt(0);
                this.writeResourceLocation(blockPredicate.getLocation());
            } else {
                this.writeVarInt(blockPredicate.getHolders().length + 1);
                for (int holder : blockPredicate.getHolders()) {
                    this.writeVarInt(holder);
                }
            }
        }

        if (blockPredicate.getProperties() == null) {
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            for (AdventureModePredicate.PropertyMatcher matcher : blockPredicate.getProperties()) {
                this.writeString(matcher.getName());
                if (matcher.getValue() != null) {
                    this.writeBoolean(true);
                    this.writeString(matcher.getValue());
                } else {
                    this.writeBoolean(false);
                    this.writeString(matcher.getMinValue());
                    this.writeString(matcher.getMaxValue());
                }
            }
        }

        this.writeNullable(blockPredicate.getNbt(), this::writeAnyTag);
    }

    public ToolData readToolData() {
        List<ToolData.Rule> rules = new ArrayList<>();
        int ruleCount = this.readVarInt();
        for (int i = 0; i < ruleCount; i++) {
            String location = null;
            int[] holders = null;

            int length = this.readVarInt() - 1;
            if (length == -1) {
                location = this.readResourceLocation();
            } else {
                holders = new int[length];
                for (int j = 0; j < length; j++) {
                    holders[j] = this.readVarInt();
                }
            }

            Float speed = this.readNullable(this::readFloat);
            Boolean correctForDrops = this.readNullable(this::readBoolean);
            rules.add(new ToolData.Rule(location, holders, speed, correctForDrops));
        }

        float defaultMiningSpeed = this.readFloat();
        int damagePerBlock = this.readVarInt();
        return new ToolData(rules, defaultMiningSpeed, damagePerBlock);
    }

    public void writeToolData(ToolData data) {
        this.writeVarInt(data.getRules().size());
        for (ToolData.Rule rule : data.getRules()) {
            if (rule.getLocation() != null) {
                this.writeVarInt(0);
                this.writeResourceLocation(rule.getLocation());
            } else {
                this.writeVarInt(rule.getHolders().length + 1);
                for (int holder : rule.getHolders()) {
                    this.writeVarInt(holder);
                }
            }

            this.writeNullable(rule.getSpeed(), this::writeFloat);
            this.writeNullable(rule.getCorrectForDrops(), this::writeBoolean);
        }

        this.writeFloat(data.getDefaultMiningSpeed());
        this.writeVarInt(data.getDamagePerBlock());
    }

    public ItemAttributeModifiers readItemAttributeModifiers() {
        List<ItemAttributeModifiers.Entry> modifiers = new ArrayList<>();
        int modifierCount = this.readVarInt();
        for (int i = 0; i < modifierCount; i++) {
            int attribute = this.readVarInt();

            UUID id = this.readUUID();
            String name = this.readString();
            double amount = this.readDouble();
            ModifierOperation operation = ModifierOperation.from(this.readVarInt());
            ItemAttributeModifiers.AttributeModifier modifier = new ItemAttributeModifiers.AttributeModifier(id, name, amount, operation);

            ItemAttributeModifiers.EquipmentSlotGroup slot = ItemAttributeModifiers.EquipmentSlotGroup.from(this.readVarInt());
            modifiers.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
        }

        return new ItemAttributeModifiers(modifiers, this.readBoolean());
    }

    public void writeItemAttributeModifiers(ItemAttributeModifiers modifiers) {
        this.writeVarInt(modifiers.getModifiers().size());
        for (ItemAttributeModifiers.Entry modifier : modifiers.getModifiers()) {
            this.writeVarInt(modifier.getAttribute());

            this.writeUUID(modifier.getModifier().getId());
            this.writeString(modifier.getModifier().getName());
            this.writeDouble(modifier.getModifier().getAmount());
            this.writeVarInt(modifier.getModifier().getOperation().ordinal());

            this.writeVarInt(modifier.getSlot().ordinal());
        }

        this.writeBoolean(modifiers.isShowInTooltip());
    }

    public DyedItemColor readDyedItemColor() {
        return new DyedItemColor(this.readInt(), this.readBoolean());
    }

    public void writeDyedItemColor(DyedItemColor itemColor) {
        this.writeInt(itemColor.getRgb());
        this.writeBoolean(itemColor.isShowInTooltip());
    }

    public PotionContents readPotionContents() {
        int potionId = this.readBoolean() ? this.readVarInt() : -1;
        int customColor = this.readBoolean() ? this.readInt() : -1;

        Int2ObjectMap<MobEffectDetails> customEffects = new Int2ObjectOpenHashMap<>();
        int effectCount = this.readVarInt();
        for (int i = 0; i < effectCount; i++) {
            customEffects.put(this.readVarInt(), this.readEffectDetails());
        }
        return new PotionContents(potionId, customColor, customEffects);
    }

    public void writePotionContents(PotionContents contents) {
        if (contents.getPotionId() < 0) {
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            this.writeVarInt(contents.getPotionId());
        }

        if (contents.getCustomColor() < 0) {
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            this.writeInt(contents.getCustomColor());
        }

        this.writeVarInt(contents.getCustomEffects().size());
        for (Int2ObjectMap.Entry<MobEffectDetails> entry : contents.getCustomEffects().int2ObjectEntrySet()) {
            this.writeVarInt(entry.getIntKey());
            this.writeEffectDetails(entry.getValue());
        }
    }

    public FoodProperties readFoodProperties() {
        int nutrition = this.readVarInt();
        float saturationModifier = this.readFloat();
        boolean canAlwaysEat = this.readBoolean();
        float eatSeconds = this.readFloat();

        List<FoodProperties.PossibleEffect> effects = new ArrayList<>();
        int effectCount = this.readVarInt();
        for (int i = 0; i < effectCount; i++) {
            effects.add(new FoodProperties.PossibleEffect(this.readEffectDetails(), this.readFloat()));
        }

        return new FoodProperties(nutrition, saturationModifier, canAlwaysEat, eatSeconds, effects);
    }

    public void writeFoodProperties(FoodProperties properties) {
        this.writeVarInt(properties.getNutrition());
        this.writeFloat(properties.getSaturationModifier());
        this.writeBoolean(properties.isCanAlwaysEat());
        this.writeFloat(properties.getEatSeconds());

        this.writeVarInt(properties.getEffects().size());
        for (FoodProperties.PossibleEffect effect : properties.getEffects()) {
            this.writeEffectDetails(effect.getEffect());
            this.writeFloat(effect.getProbability());
        }
    }

    public MobEffectDetails readEffectDetails() {
        int amplifier = this.readVarInt();
        int duration = this.readVarInt();
        boolean ambient = this.readBoolean();
        boolean showParticles = this.readBoolean();
        boolean showIcon = this.readBoolean();
        MobEffectDetails hiddenEffect = this.readNullable(this::readEffectDetails);
        return new MobEffectDetails(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
    }

    public void writeEffectDetails(MobEffectDetails details) {
        this.writeVarInt(details.getAmplifier());
        this.writeVarInt(details.getDuration());
        this.writeBoolean(details.isAmbient());
        this.writeBoolean(details.isShowParticles());
        this.writeBoolean(details.isShowIcon());
        this.writeNullable(details.getHiddenEffect(), this::writeEffectDetails);
    }

    public SuspiciousStewEffect readStewEffect() {
        return new SuspiciousStewEffect(this.readVarInt(), this.readVarInt());
    }

    public void writeStewEffect(SuspiciousStewEffect effect) {
        this.writeVarInt(effect.getMobEffectId());
        this.writeVarInt(effect.getDuration());
    }

    public WritableBookContent readWritableBookContent() {
        List<Filterable<String>> pages = new ArrayList<>();
        int pageCount = this.readVarInt();
        for (int i = 0; i < pageCount; i++) {
            pages.add(this.readFilterable(this::readString));
        }

        return new WritableBookContent(pages);
    }

    public void writeWritableBookContent(WritableBookContent content) {
        this.writeVarInt(content.getPages().size());
        for (Filterable<String> page : content.getPages()) {
            this.writeFilterable(page, this::writeString);
        }
    }

    public WrittenBookContent readWrittenBookContent() {
        Filterable<String> title = this.readFilterable(this::readString);
        String author = this.readString();
        int generation = this.readVarInt();

        List<Filterable<Component>> pages = new ArrayList<>();
        int pageCount = this.readVarInt();
        for (int i = 0; i < pageCount; i++) {
            pages.add(this.readFilterable(this::readComponent));
        }

        boolean resolved = this.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public void writeWrittenBookContent(WrittenBookContent content) {
        this.writeFilterable(content.getTitle(), this::writeString);
        this.writeString(content.getAuthor());
        this.writeVarInt(content.getGeneration());

        this.writeVarInt(content.getPages().size());
        for (Filterable<Component> page : content.getPages()) {
            this.writeFilterable(page, this::writeComponent);
        }

        this.writeBoolean(content.isResolved());
    }

    public ArmorTrim readArmorTrim() {
        Holder<ArmorTrim.TrimMaterial> material = this.readHolder(this::readTrimMaterial);
        Holder<ArmorTrim.TrimPattern> pattern = this.readHolder(this::readTrimPattern);
        boolean showInTooltip = this.readBoolean();
        return new ArmorTrim(material, pattern, showInTooltip);
    }

    public void writeArmorTrim(ArmorTrim trim) {
        this.writeHolder(trim.material(), this::writeTrimMaterial);
        this.writeHolder(trim.pattern(), this::writeTrimPattern);
        this.writeBoolean(trim.showInTooltip());
    }

    public ArmorTrim.TrimMaterial readTrimMaterial() {
        String assetName = this.readString();
        int ingredientId = this.readVarInt();
        float itemModelIndex = this.readFloat();

        Int2ObjectMap<String> overrideArmorMaterials = new Int2ObjectOpenHashMap<>();
        int overrideCount = this.readVarInt();
        for (int i = 0; i < overrideCount; i++) {
            overrideArmorMaterials.put(this.readVarInt(), this.readString());
        }

        Component description = this.readComponent();
        return new ArmorTrim.TrimMaterial(assetName, ingredientId, itemModelIndex, overrideArmorMaterials, description);
    }

    public void writeTrimMaterial(ArmorTrim.TrimMaterial material) {
        this.writeString(material.assetName());
        this.writeVarInt(material.ingredientId());
        this.writeFloat(material.itemModelIndex());

        this.writeVarInt(material.overrideArmorMaterials().size());
        for (Int2ObjectMap.Entry<String> entry : material.overrideArmorMaterials().int2ObjectEntrySet()) {
            this.writeVarInt(entry.getIntKey());
            this.writeString(entry.getValue());
        }

        this.writeComponent(material.description());
    }

    public ArmorTrim.TrimPattern readTrimPattern() {
        String assetId = this.readResourceLocation();
        int templateItemId = this.readVarInt();
        Component description = this.readComponent();
        boolean decal = this.readBoolean();
        return new ArmorTrim.TrimPattern(assetId, templateItemId, description, decal);
    }

    public void writeTrimPattern(ArmorTrim.TrimPattern pattern) {
        this.writeResourceLocation(pattern.assetId());
        this.writeVarInt(pattern.templateItemId());
        this.writeComponent(pattern.description());
        this.writeBoolean(pattern.decal());
    }

    public Holder<Instrument> readInstrument() {
        return this.readHolder(() -> {
            Sound soundEvent = this.readById(BuiltinSound::from, this::readSoundEvent);
            int useDuration = this.readVarInt();
            float range = this.readFloat();
            return new Instrument(soundEvent, useDuration, range);
        });
    }

    public void writeInstrument(Holder<Instrument> instrumentHolder) {
        this.writeHolder(instrumentHolder, (instrument) -> {
            if (instrument.getSoundEvent() instanceof CustomSound) {
                this.writeVarInt(0);
                this.writeSoundEvent(instrument.getSoundEvent());
            } else {
                this.writeVarInt(((BuiltinSound) instrument.getSoundEvent()).ordinal() + 1);
            }

            this.writeVarInt(instrument.getUseDuration());
            this.writeFloat(instrument.getRange());
        });
    }

    public NbtList<?> readRecipes() {
        return this.readAnyTag(NbtType.LIST);
    }

    public void writeRecipes(NbtList<?> recipes) {
        this.writeAnyTag(recipes);
    }

    public LodestoneTracker readLodestoneTarget() {
        return new LodestoneTracker(this.readNullable(this::readGlobalPos), this.readBoolean());
    }

    public void writeLodestoneTarget(LodestoneTracker target) {
        this.writeNullable(target.getPos(), this::writeGlobalPos);
        this.writeBoolean(target.isTracked());
    }

    public Fireworks readFireworks() {
        int flightDuration = this.readVarInt();

        List<Fireworks.FireworkExplosion> explosions = new ArrayList<>();
        int explosionCount = this.readVarInt();
        for (int i = 0; i < explosionCount; i++) {
            explosions.add(this.readFireworkExplosion());
        }

        return new Fireworks(flightDuration, explosions);
    }

    public void writeFireworks(Fireworks fireworks) {
        this.writeVarInt(fireworks.getFlightDuration());

        this.writeVarInt(fireworks.getExplosions().size());
        for (Fireworks.FireworkExplosion explosion : fireworks.getExplosions()) {
            this.writeFireworkExplosion(explosion);
        }
    }

    public Fireworks.FireworkExplosion readFireworkExplosion() {
        int shapeId = this.readVarInt();

        int[] colors = new int[this.readVarInt()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = this.readInt();
        }

        int[] fadeColors = new int[this.readVarInt()];
        for (int i = 0; i < fadeColors.length; i++) {
            fadeColors[i] = this.readInt();
        }

        boolean hasTrail = this.readBoolean();
        boolean hasTwinkle = this.readBoolean();
        return new Fireworks.FireworkExplosion(shapeId, colors, fadeColors, hasTrail, hasTwinkle);
    }

    public void writeFireworkExplosion(Fireworks.FireworkExplosion explosion) {
        this.writeVarInt(explosion.getShapeId());

        this.writeVarInt(explosion.getColors().length);
        for (int color : explosion.getColors()) {
            this.writeInt(color);
        }

        this.writeVarInt(explosion.getFadeColors().length);
        for (int fadeColor : explosion.getFadeColors()) {
            this.writeInt(fadeColor);
        }

        this.writeBoolean(explosion.isHasTrail());
        this.writeBoolean(explosion.isHasTwinkle());
    }

    public GameProfile readResolvableProfile() {
        String name = this.readNullable(this::readString);
        UUID id = this.readNullable(this::readUUID);
        GameProfile profile = new GameProfile(id, name);

        List<GameProfile.Property> properties = new ArrayList<>();
        int propertyCount = this.readVarInt();
        for (int i = 0; i < propertyCount; i++) {
            properties.add(this.readProperty());
        }
        profile.setProperties(properties);

        return profile;
    }

    public void writeResolvableProfile(GameProfile profile) {
        this.writeNullable(profile.getName(), this::writeString);
        this.writeNullable(profile.getId(), this::writeUUID);

        this.writeVarInt(profile.getProperties().size());
        for (GameProfile.Property property : profile.getProperties()) {
            this.writeProperty(property);
        }
    }

    public BannerPatternLayer readBannerPatternLayer() {
        return new BannerPatternLayer(this.readHolder(this::readBannerPattern), this.readVarInt());
    }

    public void writeBannerPatternLayer(BannerPatternLayer patternLayer) {
        this.writeHolder(patternLayer.getPattern(), this::writeBannerPattern);
        this.writeVarInt(patternLayer.getColorId());
    }

    public BannerPatternLayer.BannerPattern readBannerPattern() {
        return new BannerPatternLayer.BannerPattern(this.readResourceLocation(), this.readString());
    }

    public void writeBannerPattern(BannerPatternLayer.BannerPattern pattern) {
        this.writeResourceLocation(pattern.getAssetId());
        this.writeString(pattern.getTranslationKey());
    }

    public BlockStateProperties readBlockStateProperties() {
        Map<String, String> properties = new HashMap<>();
        int propertyCount = this.readVarInt();
        for (int i = 0; i < propertyCount; i++) {
            properties.put(this.readString(), this.readString());
        }

        return new BlockStateProperties(properties);
    }

    public void writeBlockStateProperties(BlockStateProperties props) {
        this.writeVarInt(props.getProperties().size());
        for (Map.Entry<String, String> prop : props.getProperties().entrySet()) {
            this.writeString(prop.getKey());
            this.writeString(prop.getValue());
        }
    }

    public BeehiveOccupant readBeehiveOccupant() {
        return new BeehiveOccupant(this.readCompoundTag(), this.readVarInt(), this.readVarInt());
    }

    public void writeBeehiveOccupant(BeehiveOccupant occupant) {
        this.writeAnyTag(occupant.getEntityData());
        this.writeVarInt(occupant.getTicksInHive());
        this.writeVarInt(occupant.getMinTicksInHive());
    }

    public String readLock() {
        return this.readAnyTag(NbtType.STRING);
    }

    public void writeLock(String key) {
        this.writeAnyTag(key);
    }
}
