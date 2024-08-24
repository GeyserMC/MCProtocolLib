package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class ItemCodecHelper extends MinecraftCodecHelper {
    public static final ItemCodecHelper INSTANCE = new ItemCodecHelper();

    public <T> Filterable<T> readFilterable(ByteBuf buf, Function<ByteBuf, T> reader) {
        T raw = reader.apply(buf);
        T filtered = this.readNullable(buf, reader);
        return new Filterable<>(raw, filtered);
    }

    public <T> void writeFilterable(ByteBuf buf, Filterable<T> filterable, BiConsumer<ByteBuf, T> writer) {
        writer.accept(buf, filterable.getRaw());
        this.writeNullable(buf, filterable.getOptional(), writer);
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
        List<AdventureModePredicate.BlockPredicate> predicates = this.readList(buf, this::readBlockPredicate);
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
        HolderSet holderSet = this.readNullable(buf, this::readHolderSet);
        List<AdventureModePredicate.PropertyMatcher> propertyMatchers = this.readNullable(buf, (input) -> {
            List<AdventureModePredicate.PropertyMatcher> matchers = new ArrayList<>();
            int matcherCount = this.readVarInt(input);
            for (int i = 0; i < matcherCount; i++) {
                String name = this.readString(input);
                if (input.readBoolean()) {
                    matchers.add(new AdventureModePredicate.PropertyMatcher(name, this.readString(input), null, null));
                } else {
                    matchers.add(new AdventureModePredicate.PropertyMatcher(name, null, this.readString(input), this.readString(input)));
                }
            }
            return matchers;
        });

        return new AdventureModePredicate.BlockPredicate(holderSet, propertyMatchers, this.readNullable(buf, this::readCompoundTag));
    }

    public void writeBlockPredicate(ByteBuf buf, AdventureModePredicate.BlockPredicate blockPredicate) {
        this.writeNullable(buf, blockPredicate.getBlocks(), this::writeHolderSet);
        this.writeNullable(buf, blockPredicate.getProperties(), (output, properties) -> {
            buf.writeBoolean(true);
            for (AdventureModePredicate.PropertyMatcher matcher : properties) {
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
        });

        this.writeNullable(buf, blockPredicate.getNbt(), this::writeAnyTag);
    }

    public HolderSet readHolderSet(ByteBuf buf) {
        int length = this.readVarInt(buf) - 1;
        if (length == -1) {
            return new HolderSet(this.readResourceLocation(buf));
        } else {
            int[] holders = new int[length];
            for (int i = 0; i < length; i++) {
                holders[i] = this.readVarInt(buf);
            }

            return new HolderSet(holders);
        }
    }

    public void writeHolderSet(ByteBuf buf, HolderSet holderSet) {
        if (holderSet.getLocation() != null) {
            this.writeVarInt(buf, 0);
            this.writeResourceLocation(buf, holderSet.getLocation());
        } else {
            assert holderSet.getHolders() != null;
            this.writeVarInt(buf, holderSet.getHolders().length + 1);
            for (int holder : holderSet.getHolders()) {
                this.writeVarInt(buf, holder);
            }
        }
    }

    public ToolData readToolData(ByteBuf buf) {
        List<ToolData.Rule> rules = this.readList(buf, (input) -> {
            HolderSet holderSet = this.readHolderSet(input);

            Float speed = this.readNullable(input, ByteBuf::readFloat);
            Boolean correctForDrops = this.readNullable(input, ByteBuf::readBoolean);
            return new ToolData.Rule(holderSet, speed, correctForDrops);
        });

        float defaultMiningSpeed = buf.readFloat();
        int damagePerBlock = this.readVarInt(buf);
        return new ToolData(rules, defaultMiningSpeed, damagePerBlock);
    }

    public void writeToolData(ByteBuf buf, ToolData data) {
        this.writeList(buf, data.getRules(), (output, rule) -> {
            this.writeHolderSet(output, rule.getBlocks());
            this.writeNullable(output, rule.getSpeed(), ByteBuf::writeFloat);
            this.writeNullable(output, rule.getCorrectForDrops(), ByteBuf::writeBoolean);
        });

        buf.writeFloat(data.getDefaultMiningSpeed());
        this.writeVarInt(buf, data.getDamagePerBlock());
    }

    public ItemAttributeModifiers readItemAttributeModifiers(ByteBuf buf) {
        List<ItemAttributeModifiers.Entry> modifiers = this.readList(buf, (input) -> {
            int attribute = this.readVarInt(input);

            Key id = this.readResourceLocation(input);
            double amount = input.readDouble();
            ModifierOperation operation = ModifierOperation.from(this.readVarInt(input));
            ItemAttributeModifiers.AttributeModifier modifier = new ItemAttributeModifiers.AttributeModifier(id, amount, operation);

            ItemAttributeModifiers.EquipmentSlotGroup slot = ItemAttributeModifiers.EquipmentSlotGroup.from(this.readVarInt(input));
            return new ItemAttributeModifiers.Entry(attribute, modifier, slot);
        });

        return new ItemAttributeModifiers(modifiers, buf.readBoolean());
    }

    public void writeItemAttributeModifiers(ByteBuf buf, ItemAttributeModifiers modifiers) {
        this.writeList(buf, modifiers.getModifiers(), (output, entry) -> {
            this.writeVarInt(output, entry.getAttribute());
            this.writeResourceLocation(output, entry.getModifier().getId());
            output.writeDouble(entry.getModifier().getAmount());
            this.writeVarInt(output, entry.getModifier().getOperation().ordinal());
            this.writeVarInt(output, entry.getSlot().ordinal());
        });

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

        List<MobEffectInstance> customEffects = this.readList(buf, this::readEffectInstance);
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

        this.writeList(buf, contents.getCustomEffects(), this::writeEffectInstance);
    }

    public FoodProperties readFoodProperties(ByteBuf buf) {
        int nutrition = this.readVarInt(buf);
        float saturationModifier = buf.readFloat();
        boolean canAlwaysEat = buf.readBoolean();
        float eatSeconds = buf.readFloat();
        ItemStack usingConvertsTo = this.readNullable(buf, this::readOptionalItemStack);

        List<FoodProperties.PossibleEffect> effects = this.readList(buf, (input) -> {
            MobEffectInstance effect = this.readEffectInstance(input);
            float probability = input.readFloat();
            return new FoodProperties.PossibleEffect(effect, probability);
        });

        return new FoodProperties(nutrition, saturationModifier, canAlwaysEat, eatSeconds, usingConvertsTo, effects);
    }

    public void writeFoodProperties(ByteBuf buf, FoodProperties properties) {
        this.writeVarInt(buf, properties.getNutrition());
        buf.writeFloat(properties.getSaturationModifier());
        buf.writeBoolean(properties.isCanAlwaysEat());
        buf.writeFloat(properties.getEatSeconds());
        this.writeNullable(buf, properties.getUsingConvertsTo(), this::writeOptionalItemStack);

        this.writeList(buf, properties.getEffects(), (output, effect) -> {
            this.writeEffectInstance(output, effect.getEffect());
            output.writeFloat(effect.getProbability());
        });
    }

    public MobEffectInstance readEffectInstance(ByteBuf buf) {
        Effect effect = this.readEffect(buf);
        return new MobEffectInstance(effect, this.readEffectDetails(buf));
    }

    public MobEffectDetails readEffectDetails(ByteBuf buf) {
        int amplifier = this.readVarInt(buf);
        int duration = this.readVarInt(buf);
        boolean ambient = buf.readBoolean();
        boolean showParticles = buf.readBoolean();
        boolean showIcon = buf.readBoolean();
        MobEffectDetails hiddenEffect = this.readNullable(buf, this::readEffectDetails);
        return new MobEffectDetails(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
    }

    public void writeEffectInstance(ByteBuf buf, MobEffectInstance instance) {
        this.writeEffect(buf, instance.getEffect());
        this.writeEffectDetails(buf, instance.getDetails());
    }

    public void writeEffectDetails(ByteBuf buf, MobEffectDetails details) {
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
        List<Filterable<String>> pages = this.readList(buf, (input) -> this.readFilterable(input, this::readString));
        return new WritableBookContent(pages);
    }

    public void writeWritableBookContent(ByteBuf buf, WritableBookContent content) {
        this.writeList(buf, content.getPages(), (output, page) -> this.writeFilterable(output, page, this::writeString));
    }

    public WrittenBookContent readWrittenBookContent(ByteBuf buf) {
        Filterable<String> title = this.readFilterable(buf, this::readString);
        String author = this.readString(buf);
        int generation = this.readVarInt(buf);

        List<Filterable<Component>> pages = this.readList(buf, (input) -> this.readFilterable(input, this::readComponent));
        boolean resolved = buf.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public void writeWrittenBookContent(ByteBuf buf, WrittenBookContent content) {
        this.writeFilterable(buf, content.getTitle(), this::writeString);
        this.writeString(buf, content.getAuthor());
        this.writeVarInt(buf, content.getGeneration());

        this.writeList(buf, content.getPages(), (output, page) -> this.writeFilterable(output, page, this::writeComponent));

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
        Key assetId = this.readResourceLocation(buf);
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
            float useDuration = input.readFloat();
            float range = input.readFloat();
            Component description = this.readComponent(input);
            return new Instrument(soundEvent, useDuration, range, description);
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

            buf.writeFloat(instrument.getUseDuration());
            buf.writeFloat(instrument.getRange());
            this.writeComponent(buf, instrument.getDescription());
        });
    }

    public NbtList<?> readRecipes(ByteBuf buf) {
        return this.readAnyTag(buf, NbtType.LIST);
    }

    public void writeRecipes(ByteBuf buf, NbtList<?> recipes) {
        this.writeAnyTag(buf, recipes);
    }

    public JukeboxPlayable readJukeboxPlayable(ByteBuf buf) {
        Holder<JukeboxPlayable.JukeboxSong> songHolder = null;
        Key songLocation = null;
        if (buf.readBoolean()) {
            songHolder = this.readHolder(buf, this::readJukeboxSong);
        } else {
            songLocation = this.readResourceLocation(buf);
        }
        boolean showInTooltip = buf.readBoolean();
        return new JukeboxPlayable(songHolder, songLocation, showInTooltip);
    }

    public void writeJukeboxPlayable(ByteBuf buf, JukeboxPlayable playable) {
        buf.writeBoolean(playable.songHolder() != null);
        if (playable.songHolder() != null) {
            this.writeHolder(buf, playable.songHolder(), this::writeJukeboxSong);
        } else {
            this.writeResourceLocation(buf, playable.songLocation());
        }
        buf.writeBoolean(playable.showInTooltip());
    }

    public JukeboxPlayable.JukeboxSong readJukeboxSong(ByteBuf buf) {
        Sound soundEvent = this.readById(buf, BuiltinSound::from, this::readSoundEvent);
        Component description = this.readComponent(buf);
        float lengthInSeconds = buf.readFloat();
        int comparatorOutput = this.readVarInt(buf);
        return new JukeboxPlayable.JukeboxSong(soundEvent, description, lengthInSeconds, comparatorOutput);
    }

    public void writeJukeboxSong(ByteBuf buf, JukeboxPlayable.JukeboxSong song) {
        if (song.soundEvent() instanceof CustomSound) {
            this.writeVarInt(buf, 0);
            this.writeSoundEvent(buf, song.soundEvent());
        } else {
            this.writeVarInt(buf, ((BuiltinSound) song.soundEvent()).ordinal() + 1);
        }
        this.writeComponent(buf, song.description());
        buf.writeFloat(song.lengthInSeconds());
        this.writeVarInt(buf, song.comparatorOutput());
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

        List<GameProfile.Property> properties = this.readList(buf, this::readProperty);
        profile.setProperties(properties);

        return profile;
    }

    public void writeResolvableProfile(ByteBuf buf, GameProfile profile) {
        this.writeNullable(buf, profile.getName(), this::writeString);
        this.writeNullable(buf, profile.getId(), this::writeUUID);

        this.writeList(buf, profile.getProperties(), this::writeProperty);
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
