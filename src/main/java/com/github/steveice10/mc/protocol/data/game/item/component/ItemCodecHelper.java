package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.CheckedBiConsumer;
import com.github.steveice10.mc.protocol.CheckedFunction;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.data.game.level.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.CustomSound;
import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ItemCodecHelper extends MinecraftCodecHelper {
    public static ItemCodecHelper INSTANCE = new ItemCodecHelper();

    public ItemCodecHelper() {
        super(Int2ObjectMaps.emptyMap(), Collections.emptyMap());
    }


    public <T, E extends Throwable> Filterable<T> readFilterable(ByteBuf buf, CheckedFunction<ByteBuf, T, E> reader) throws E {
        T raw = reader.apply(buf);
        T filtered = null;
        if (buf.readBoolean()) {
            filtered = reader.apply(buf);
        }
        return new Filterable<>(raw, filtered);
    }

    public <T, E extends Throwable> void writeFilterable(ByteBuf buf, Filterable<T> filterable, CheckedBiConsumer<ByteBuf, T, E> writer) throws E {
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
        for (int i = 0; i < this.readVarInt(buf); i++) {
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

    public AdventureModePredicate readAdventureModePredicate(ByteBuf buf) throws IOException {
        List<AdventureModePredicate.BlockPredicate> predicates = new ArrayList<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            predicates.add(this.readBlockPredicate(buf));
        }

        return new AdventureModePredicate(predicates, buf.readBoolean());
    }

    public void writeAdventureModePredicate(ByteBuf buf, AdventureModePredicate adventureModePredicate) throws IOException {
        this.writeVarInt(buf, adventureModePredicate.getPredicates().size());
        for (AdventureModePredicate.BlockPredicate predicate : adventureModePredicate.getPredicates()) {
            this.writeBlockPredicate(buf, predicate);
        }

        buf.writeBoolean(adventureModePredicate.isShowInTooltip());
    }

    public AdventureModePredicate.BlockPredicate readBlockPredicate(ByteBuf buf) throws IOException {
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
            for (int i = 0; i < this.readVarInt(buf); i++) {
                String name = this.readString(buf);
                if (buf.readBoolean()) {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, this.readString(buf), null, null));
                } else {
                    propertyMatchers.add(new AdventureModePredicate.PropertyMatcher(name, null, this.readString(buf), this.readString(buf)));
                }
            }
        }

        return new AdventureModePredicate.BlockPredicate(location, holders, propertyMatchers, this.readNullable(buf, this::readAnyTag));
    }

    public void writeBlockPredicate(ByteBuf buf, AdventureModePredicate.BlockPredicate blockPredicate) throws IOException {
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

    public ItemAttributeModifiers readItemAttributeModifiers(ByteBuf buf) {
        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            int attribute = this.readVarInt(buf);

            UUID id = this.readUUID(buf);
            String name = this.readString(buf);
            double amount = buf.readDouble();
            ModifierOperation operation = ModifierOperation.from(this.readVarInt(buf));
            ItemAttributeModifiers.AttributeModifier modifier = new ItemAttributeModifiers.AttributeModifier(id, name, amount, operation);

            ItemAttributeModifiers.EquipmentSlotGroup slot = ItemAttributeModifiers.EquipmentSlotGroup.from(this.readVarInt(buf));
            entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
        }

        return new ItemAttributeModifiers(entries, buf.readBoolean());
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

        Int2ObjectMap<PotionContents.MobEffectDetails> customEffects = new Int2ObjectOpenHashMap<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            customEffects.put(this.readVarInt(buf), this.readEffectDetails(buf));
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
        for (Int2ObjectMap.Entry<PotionContents.MobEffectDetails> entry : contents.getCustomEffects().int2ObjectEntrySet()) {
            this.writeVarInt(buf, entry.getIntKey());
            this.writeEffectDetails(buf, entry.getValue());
        }
    }

    public PotionContents.MobEffectDetails readEffectDetails(ByteBuf buf) {
        int amplifier = this.readVarInt(buf);
        int duration = this.readVarInt(buf);
        boolean ambient = buf.readBoolean();
        boolean showParticles = buf.readBoolean();
        boolean showIcon = buf.readBoolean();
        PotionContents.MobEffectDetails hiddenEffect = this.readNullable(buf, this::readEffectDetails);
        return new PotionContents.MobEffectDetails(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
    }

    public void writeEffectDetails(ByteBuf buf, PotionContents.MobEffectDetails details) {
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
        for (int i = 0; i < this.readVarInt(buf); i++) {
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

    public WrittenBookContent readWrittenBookContent(ByteBuf buf) throws IOException {
        Filterable<String> title = this.readFilterable(buf, this::readString);
        String author = this.readString(buf);
        int generation = this.readVarInt(buf);

        List<Filterable<Component>> pages = new ArrayList<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            pages.add(this.readFilterable(buf, this::readComponent));
        }

        boolean resolved = buf.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public void writeWrittenBookContent(ByteBuf buf, WrittenBookContent content) throws IOException {
        this.writeFilterable(buf, content.getTitle(), this::writeString);
        this.writeString(buf, content.getAuthor());
        this.writeVarInt(buf, content.getGeneration());

        this.writeVarInt(buf, content.getPages().size());
        for (Filterable<Component> page : content.getPages()) {
            this.writeFilterable(buf, page, this::writeComponent);
        }

        buf.writeBoolean(content.isResolved());
    }

    // TODO: Could be simplified using readById method, would require more enums. Worth it?
    public ArmorTrim readArmorTrim(ByteBuf buf) throws IOException {
        ArmorTrim.TrimMaterial material;
        int materialId = this.readVarInt(buf) - 1;
        if (materialId == -1) {
            String assetName = this.readString(buf);
            int ingredientId = this.readVarInt(buf);
            float itemModelIndex = buf.readFloat();

            Int2ObjectMap<String> overrideArmorMaterials = new Int2ObjectOpenHashMap<>();
            for (int i = 0; i < this.readVarInt(buf); i++) {
                overrideArmorMaterials.put(this.readVarInt(buf), this.readString(buf));
            }

            Component description = this.readComponent(buf);
            material = new ArmorTrim.TrimMaterial(materialId, assetName, ingredientId, itemModelIndex, overrideArmorMaterials, description);
        } else {
            material = new ArmorTrim.TrimMaterial(materialId, null, 0, 0, null, null);
        }

        ArmorTrim.TrimPattern pattern;
        int patternId = this.readVarInt(buf) - 1;
        if (patternId == -1) {
            String assetId = this.readResourceLocation(buf);
            int templateItemId = this.readVarInt(buf);
            Component description = this.readComponent(buf);
            boolean decal = buf.readBoolean();
            pattern = new ArmorTrim.TrimPattern(patternId, assetId, templateItemId, description, decal);
        } else {
            pattern = new ArmorTrim.TrimPattern(patternId, null, 0, null, false);
        }

        boolean showInTooltip = buf.readBoolean();
        return new ArmorTrim(material, pattern, showInTooltip);
    }

    public void writeArmorTrim(ByteBuf buf, ArmorTrim trim) throws IOException {
        ArmorTrim.TrimMaterial material = trim.getMaterial();
        this.writeVarInt(buf, material.getMaterialId() + 1);
        if (material.getMaterialId() == -1) {
            this.writeString(buf, material.getAssetName());
            this.writeVarInt(buf, material.getIngredientId());
            buf.writeFloat(material.getItemModelIndex());

            this.writeVarInt(buf, material.getOverrideArmorMaterials().size());
            for (Int2ObjectMap.Entry<String> entry : material.getOverrideArmorMaterials().int2ObjectEntrySet()) {
                this.writeVarInt(buf, entry.getIntKey());
                this.writeString(buf, entry.getValue());
            }

            this.writeComponent(buf, material.getDescription());
        }

        ArmorTrim.TrimPattern pattern = trim.getPattern();
        this.writeVarInt(buf, pattern.getPatternId() + 1);
        if (pattern.getPatternId() == -1) {
            this.writeResourceLocation(buf, pattern.getAssetId());
            this.writeVarInt(buf, pattern.getTemplateItemId());
            this.writeComponent(buf, pattern.getDescription());
            buf.writeBoolean(pattern.isDecal());
        }

        buf.writeBoolean(trim.isShowInTooltip());
    }

    public Instrument readInstrument(ByteBuf buf) {
        int instrumentId = this.readVarInt(buf) - 1;
        if (instrumentId == -1) {
            Sound soundEvent = this.readById(buf, BuiltinSound::from, this::readSoundEvent);
            int useDuration = this.readVarInt(buf);
            float range = buf.readFloat();
            return new Instrument(instrumentId, soundEvent, useDuration, range);
        } else {
            return new Instrument(instrumentId, null, 0, 0);
        }
    }

    public void writeInstrument(ByteBuf buf, Instrument instrument) {
        this.writeVarInt(buf, instrument.getInstrumentId() + 1);
        if (instrument.getInstrumentId() == -1) {
            if (instrument.getSoundEvent() instanceof CustomSound) {
                this.writeVarInt(buf, 0);
                this.writeSoundEvent(buf, instrument.getSoundEvent());
            } else {
                this.writeVarInt(buf, ((BuiltinSound) instrument.getSoundEvent()).ordinal() + 1);
            }

            this.writeVarInt(buf, instrument.getUseDuration());
            buf.writeFloat(instrument.getRange());
        }
    }

    public LodestoneTarget readLodestoneTarget(ByteBuf buf) {
        return new LodestoneTarget(this.readGlobalPos(buf), buf.readBoolean());
    }

    public void writeLodestoneTarget(ByteBuf buf, LodestoneTarget target) {
        this.writeGlobalPos(buf, target.getPos());
        buf.writeBoolean(target.isTracked());
    }

    public Fireworks readFireworks(ByteBuf buf) {
        int flightDuration = this.readVarInt(buf);

        List<Fireworks.FireworkExplosion> explosions = new ArrayList<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
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
        String name = this.readString(buf);
        UUID id = this.readNullable(buf, this::readUUID);
        GameProfile profile = new GameProfile(id, name);

        List<GameProfile.Property> properties = new ArrayList<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            properties.add(this.readProperty(buf));
        }
        profile.setProperties(properties);

        return profile;
    }

    public void writeResolvableProfile(ByteBuf buf, GameProfile profile) {
        this.writeString(buf, profile.getName());
        this.writeUUID(buf, profile.getId());

        this.writeVarInt(buf, profile.getProperties().size());
        for (GameProfile.Property property : profile.getProperties()) {
            this.writeProperty(buf, property);
        }
    }

    public BannerPatternLayer readBannerPattern(ByteBuf buf) {
        return new BannerPatternLayer(this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeBannerPattern(ByteBuf buf, BannerPatternLayer pattern) {
        this.writeVarInt(buf, pattern.getPatternId());
        this.writeVarInt(buf, pattern.getColorId());
    }

    public BlockStateProperties readBlockStateProperties(ByteBuf buf) {
        Map<String, String> props = new HashMap<>();
        for (int i = 0; i < this.readVarInt(buf); i++) {
            props.put(this.readString(buf), this.readString(buf));
        }

        return new BlockStateProperties(props);
    }

    public void writeBlockStateProperties(ByteBuf buf, BlockStateProperties props) {
        this.writeVarInt(buf, props.getProperties().size());
        for (Map.Entry<String, String> prop : props.getProperties().entrySet()) {
            this.writeString(buf, prop.getKey());
            this.writeString(buf, prop.getValue());
        }
    }

    public BeehiveOccupant readBeehiveOccupant(ByteBuf buf) throws IOException {
        return new BeehiveOccupant(this.readAnyTag(buf), this.readVarInt(buf), this.readVarInt(buf));
    }

    public void writeBeehiveOccupant(ByteBuf buf, BeehiveOccupant occupant) throws IOException {
        this.writeAnyTag(buf, occupant.getEntityData());
        this.writeVarInt(buf, occupant.getTicksInHive());
        this.writeVarInt(buf, occupant.getMinTicksInHive());
    }
}
