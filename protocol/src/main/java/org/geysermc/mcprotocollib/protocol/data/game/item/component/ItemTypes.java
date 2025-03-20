package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.ModifierOperation;
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

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemTypes {
    public static <T> Filterable<T> readFilterable(ByteBuf buf, Function<ByteBuf, T> reader) {
        T raw = reader.apply(buf);
        T filtered = MinecraftTypes.readNullable(buf, reader);
        return new Filterable<>(raw, filtered);
    }

    public static <T> void writeFilterable(ByteBuf buf, Filterable<T> filterable, BiConsumer<ByteBuf, T> writer) {
        writer.accept(buf, filterable.getRaw());
        MinecraftTypes.writeNullable(buf, filterable.getOptional(), writer);
    }

    public static ItemEnchantments readItemEnchantments(ByteBuf buf) {
        Map<Integer, Integer> enchantments = new HashMap<>();
        int enchantmentCount = MinecraftTypes.readVarInt(buf);
        for (int i = 0; i < enchantmentCount; i++) {
            enchantments.put(MinecraftTypes.readVarInt(buf), MinecraftTypes.readVarInt(buf));
        }

        return new ItemEnchantments(enchantments, buf.readBoolean());
    }

    public static void writeItemEnchantments(ByteBuf buf, ItemEnchantments itemEnchantments) {
        MinecraftTypes.writeVarInt(buf, itemEnchantments.getEnchantments().size());
        for (Map.Entry<Integer, Integer> entry : itemEnchantments.getEnchantments().entrySet()) {
            MinecraftTypes.writeVarInt(buf, entry.getKey());
            MinecraftTypes.writeVarInt(buf, entry.getValue());
        }

        buf.writeBoolean(itemEnchantments.isShowInTooltip());
    }

    public static AdventureModePredicate readAdventureModePredicate(ByteBuf buf) {
        List<AdventureModePredicate.BlockPredicate> predicates = MinecraftTypes.readList(buf, ItemTypes::readBlockPredicate);
        return new AdventureModePredicate(predicates);
    }

    public static void writeAdventureModePredicate(ByteBuf buf, AdventureModePredicate adventureModePredicate) {
        MinecraftTypes.writeVarInt(buf, adventureModePredicate.getPredicates().size());
        for (AdventureModePredicate.BlockPredicate predicate : adventureModePredicate.getPredicates()) {
            ItemTypes.writeBlockPredicate(buf, predicate);
        }
    }

    public static AdventureModePredicate.BlockPredicate readBlockPredicate(ByteBuf buf) {
        HolderSet holderSet = MinecraftTypes.readNullable(buf, MinecraftTypes::readHolderSet);
        List<AdventureModePredicate.PropertyMatcher> propertyMatchers = MinecraftTypes.readNullable(buf, (input) -> {
            List<AdventureModePredicate.PropertyMatcher> matchers = new ArrayList<>();
            int matcherCount = MinecraftTypes.readVarInt(input);
            for (int i = 0; i < matcherCount; i++) {
                String name = MinecraftTypes.readString(input);
                if (input.readBoolean()) {
                    matchers.add(new AdventureModePredicate.PropertyMatcher(name, MinecraftTypes.readString(input), null, null));
                } else {
                    matchers.add(new AdventureModePredicate.PropertyMatcher(name, null, MinecraftTypes.readString(input), MinecraftTypes.readString(input)));
                }
            }
            return matchers;
        });

        return new AdventureModePredicate.BlockPredicate(holderSet, propertyMatchers, MinecraftTypes.readNullable(buf, MinecraftTypes::readCompoundTag));
    }

    public static void writeBlockPredicate(ByteBuf buf, AdventureModePredicate.BlockPredicate blockPredicate) {
        MinecraftTypes.writeNullable(buf, blockPredicate.getBlocks(), MinecraftTypes::writeHolderSet);
        MinecraftTypes.writeNullable(buf, blockPredicate.getProperties(), (output, properties) -> {
            buf.writeBoolean(true);
            for (AdventureModePredicate.PropertyMatcher matcher : properties) {
                MinecraftTypes.writeString(buf, matcher.getName());
                if (matcher.getValue() != null) {
                    buf.writeBoolean(true);
                    MinecraftTypes.writeString(buf, matcher.getValue());
                } else {
                    buf.writeBoolean(false);
                    MinecraftTypes.writeString(buf, matcher.getMinValue());
                    MinecraftTypes.writeString(buf, matcher.getMaxValue());
                }
            }
        });

        MinecraftTypes.writeNullable(buf, blockPredicate.getNbt(), MinecraftTypes::writeAnyTag);
    }

    public static ToolData readToolData(ByteBuf buf) {
        List<ToolData.Rule> rules = MinecraftTypes.readList(buf, (input) -> {
            HolderSet holderSet = MinecraftTypes.readHolderSet(input);

            Float speed = MinecraftTypes.readNullable(input, ByteBuf::readFloat);
            Boolean correctForDrops = MinecraftTypes.readNullable(input, ByteBuf::readBoolean);
            return new ToolData.Rule(holderSet, speed, correctForDrops);
        });

        float defaultMiningSpeed = buf.readFloat();
        int damagePerBlock = MinecraftTypes.readVarInt(buf);
        boolean canDestroyBlocksInCreative = buf.readBoolean();
        return new ToolData(rules, defaultMiningSpeed, damagePerBlock, canDestroyBlocksInCreative);
    }

    public static void writeToolData(ByteBuf buf, ToolData data) {
        MinecraftTypes.writeList(buf, data.getRules(), (output, rule) -> {
            MinecraftTypes.writeHolderSet(output, rule.getBlocks());
            MinecraftTypes.writeNullable(output, rule.getSpeed(), ByteBuf::writeFloat);
            MinecraftTypes.writeNullable(output, rule.getCorrectForDrops(), ByteBuf::writeBoolean);
        });

        buf.writeFloat(data.getDefaultMiningSpeed());
        MinecraftTypes.writeVarInt(buf, data.getDamagePerBlock());
        buf.writeBoolean(data.isCanDestroyBlocksInCreative());
    }

    public static Weapon readWeapon(ByteBuf buf) {
        int damagePerAttack = MinecraftTypes.readVarInt(buf);
        float disableBlockingForSeconds = buf.readFloat();
        return new Weapon(damagePerAttack, disableBlockingForSeconds);
    }

    public static void writeWeapon(ByteBuf buf, Weapon weapon) {
        MinecraftTypes.writeVarInt(buf, weapon.itemDamagePerAttack());
        buf.writeFloat(weapon.disableBlockingForSeconds());
    }

    public static Equippable readEquippable(ByteBuf buf) {
        EquipmentSlot slot = EquipmentSlot.from(MinecraftTypes.readVarInt(buf));
        Sound equipSound = MinecraftTypes.readSound(buf);
        Key model = MinecraftTypes.readNullable(buf, MinecraftTypes::readResourceLocation);
        Key cameraOverlay = MinecraftTypes.readNullable(buf, MinecraftTypes::readResourceLocation);
        HolderSet allowedEntities = MinecraftTypes.readNullable(buf, MinecraftTypes::readHolderSet);
        boolean dispensable = buf.readBoolean();
        boolean swappable = buf.readBoolean();
        boolean damageOnHurt = buf.readBoolean();
        boolean equipOnInteract = buf.readBoolean();
        return new Equippable(slot, equipSound, model, cameraOverlay, allowedEntities, dispensable, swappable, damageOnHurt, equipOnInteract);
    }

    public static void writeEquippable(ByteBuf buf, Equippable equippable) {
        MinecraftTypes.writeVarInt(buf, equippable.slot().ordinal());
        MinecraftTypes.writeSound(buf, equippable.equipSound());
        MinecraftTypes.writeNullable(buf, equippable.model(), MinecraftTypes::writeResourceLocation);
        MinecraftTypes.writeNullable(buf, equippable.cameraOverlay(), MinecraftTypes::writeResourceLocation);
        MinecraftTypes.writeNullable(buf, equippable.allowedEntities(), MinecraftTypes::writeHolderSet);
        buf.writeBoolean(equippable.dispensable());
        buf.writeBoolean(equippable.swappable());
        buf.writeBoolean(equippable.damageOnHurt());
        buf.writeBoolean(equippable.equipOnInteract());
    }

    public static BlocksAttacks readBlocksAttacks(ByteBuf buf) {
        float blockDelaySeconds = buf.readFloat();
        float disableCooldownScale = buf.readFloat();

        List<BlocksAttacks.DamageReduction> damageReductions = MinecraftTypes.readList(buf, (input) -> {
            float horizontalBlockingAngle = input.readFloat();
            HolderSet type = MinecraftTypes.readNullable(input, MinecraftTypes::readHolderSet);
            float base = input.readFloat();
            float factor = input.readFloat();
            return new BlocksAttacks.DamageReduction(horizontalBlockingAngle, type, base, factor);
        });

        BlocksAttacks.ItemDamageFunction itemDamage = new BlocksAttacks.ItemDamageFunction(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Key bypassedBy = MinecraftTypes.readNullable(buf, MinecraftTypes::readResourceLocation);
        Sound blockSound = MinecraftTypes.readNullable(buf, input -> MinecraftTypes.readById(input, BuiltinSound::from, MinecraftTypes::readSoundEvent));
        Sound disableSound = MinecraftTypes.readNullable(buf, input -> MinecraftTypes.readById(input, BuiltinSound::from, MinecraftTypes::readSoundEvent));
        return new BlocksAttacks(blockDelaySeconds, disableCooldownScale, damageReductions, itemDamage, bypassedBy, blockSound, disableSound);
    }

    public static void writeBlocksAttacks(ByteBuf buf, BlocksAttacks blocksAttacks) {
        buf.writeFloat(blocksAttacks.blockDelaySeconds());
        buf.writeFloat(blocksAttacks.disableCooldownScale());

        MinecraftTypes.writeList(buf, blocksAttacks.damageReductions(), (output, entry) -> {
            output.writeFloat(entry.horizontalBlockingAngle());
            MinecraftTypes.writeNullable(output, entry.type(), MinecraftTypes::writeHolderSet);
            output.writeFloat(entry.base());
            output.writeFloat(entry.factor());
        });

        buf.writeFloat(blocksAttacks.itemDamage().threshold());
        buf.writeFloat(blocksAttacks.itemDamage().base());
        buf.writeFloat(blocksAttacks.itemDamage().factor());
        MinecraftTypes.writeNullable(buf, blocksAttacks.bypassedBy(), MinecraftTypes::writeResourceLocation);

        MinecraftTypes.writeNullable(buf, blocksAttacks.blockSound(), (output, sound) -> {
            if (sound instanceof CustomSound) {
                MinecraftTypes.writeVarInt(output, 0);
                MinecraftTypes.writeSoundEvent(output, sound);
            } else {
                MinecraftTypes.writeVarInt(output, ((BuiltinSound)sound).ordinal() + 1);
            }
        });

        MinecraftTypes.writeNullable(buf, blocksAttacks.disableSound(), (output, sound) -> {
            if (sound instanceof CustomSound) {
                MinecraftTypes.writeVarInt(output, 0);
                MinecraftTypes.writeSoundEvent(output, sound);
            } else {
                MinecraftTypes.writeVarInt(output, ((BuiltinSound)sound).ordinal() + 1);
            }
        });
    }

    public static ItemAttributeModifiers readItemAttributeModifiers(ByteBuf buf) {
        List<ItemAttributeModifiers.Entry> modifiers = MinecraftTypes.readList(buf, (input) -> {
            int attribute = MinecraftTypes.readVarInt(input);

            Key id = MinecraftTypes.readResourceLocation(input);
            double amount = input.readDouble();
            ModifierOperation operation = ModifierOperation.from(MinecraftTypes.readVarInt(input));
            ItemAttributeModifiers.AttributeModifier modifier = new ItemAttributeModifiers.AttributeModifier(id, amount, operation);

            ItemAttributeModifiers.EquipmentSlotGroup slot = ItemAttributeModifiers.EquipmentSlotGroup.from(MinecraftTypes.readVarInt(input));
            return new ItemAttributeModifiers.Entry(attribute, modifier, slot);
        });

        return new ItemAttributeModifiers(modifiers);
    }

    public static void writeItemAttributeModifiers(ByteBuf buf, ItemAttributeModifiers modifiers) {
        MinecraftTypes.writeList(buf, modifiers.getModifiers(), (output, entry) -> {
            MinecraftTypes.writeVarInt(output, entry.getAttribute());
            MinecraftTypes.writeResourceLocation(output, entry.getModifier().getId());
            output.writeDouble(entry.getModifier().getAmount());
            MinecraftTypes.writeVarInt(output, entry.getModifier().getOperation().ordinal());
            MinecraftTypes.writeVarInt(output, entry.getSlot().ordinal());
        });
    }

    public static TooltipDisplay readTooltipDisplay(ByteBuf buf) {
        boolean hideTooltip = buf.readBoolean();
        List<DataComponentType<?>> hiddenComponents = MinecraftTypes.readList(buf, input -> DataComponentTypes.from(MinecraftTypes.readVarInt(input)));
        return new TooltipDisplay(hideTooltip, hiddenComponents);
    }

    public static void writeTooltipDisplay(ByteBuf buf, TooltipDisplay tooltipDisplay) {
        buf.writeBoolean(tooltipDisplay.hideTooltip());
        MinecraftTypes.writeList(buf, tooltipDisplay.hiddenComponents(), (output, entry) -> MinecraftTypes.writeVarInt(output, entry.getId()));
    }

    public static CustomModelData readCustomModelData(ByteBuf buf) {
        List<Float> floats = MinecraftTypes.readList(buf, ByteBuf::readFloat);
        List<Boolean> flags = MinecraftTypes.readList(buf, ByteBuf::readBoolean);
        List<String> strings = MinecraftTypes.readList(buf, MinecraftTypes::readString);
        List<Integer> colors = MinecraftTypes.readList(buf, ByteBuf::readInt);
        return new CustomModelData(floats, flags, strings, colors);
    }

    public static void writeCustomModelData(ByteBuf buf, CustomModelData modelData) {
        MinecraftTypes.writeList(buf, modelData.floats(), ByteBuf::writeFloat);
        MinecraftTypes.writeList(buf, modelData.flags(), ByteBuf::writeBoolean);
        MinecraftTypes.writeList(buf, modelData.strings(), MinecraftTypes::writeString);
        MinecraftTypes.writeList(buf, modelData.colors(), ByteBuf::writeInt);
    }

    public static PotionContents readPotionContents(ByteBuf buf) {
        int potionId = buf.readBoolean() ? MinecraftTypes.readVarInt(buf) : -1;
        int customColor = buf.readBoolean() ? buf.readInt() : -1;

        List<MobEffectInstance> customEffects = MinecraftTypes.readList(buf, ItemTypes::readEffectInstance);
        String customName = MinecraftTypes.readNullable(buf, MinecraftTypes::readString);
        return new PotionContents(potionId, customColor, customEffects, customName);
    }

    public static void writePotionContents(ByteBuf buf, PotionContents contents) {
        if (contents.getPotionId() < 0) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            MinecraftTypes.writeVarInt(buf, contents.getPotionId());
        }

        if (contents.getCustomColor() < 0) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(contents.getCustomColor());
        }

        MinecraftTypes.writeList(buf, contents.getCustomEffects(), ItemTypes::writeEffectInstance);
        MinecraftTypes.writeNullable(buf, contents.getCustomName(), MinecraftTypes::writeString);
    }

    public static FoodProperties readFoodProperties(ByteBuf buf) {
        int nutrition = MinecraftTypes.readVarInt(buf);
        float saturationModifier = buf.readFloat();
        boolean canAlwaysEat = buf.readBoolean();

        return new FoodProperties(nutrition, saturationModifier, canAlwaysEat);
    }

    public static void writeFoodProperties(ByteBuf buf, FoodProperties properties) {
        MinecraftTypes.writeVarInt(buf, properties.getNutrition());
        buf.writeFloat(properties.getSaturationModifier());
        buf.writeBoolean(properties.isCanAlwaysEat());
    }

    public static Consumable readConsumable(ByteBuf buf) {
        float consumeSeconds = buf.readFloat();
        Consumable.ItemUseAnimation animation = Consumable.ItemUseAnimation.from(MinecraftTypes.readVarInt(buf));
        Sound sound = MinecraftTypes.readSound(buf);
        boolean hasConsumeParticles = buf.readBoolean();
        List<ConsumeEffect> onConsumeEffects = MinecraftTypes.readList(buf, ItemTypes::readConsumeEffect);
        return new Consumable(consumeSeconds, animation, sound, hasConsumeParticles, onConsumeEffects);
    }

    public static void writeConsumable(ByteBuf buf, Consumable consumable) {
        buf.writeFloat(consumable.consumeSeconds());
        MinecraftTypes.writeVarInt(buf, consumable.animation().ordinal());
        MinecraftTypes.writeSound(buf, consumable.sound());
        buf.writeBoolean(consumable.hasConsumeParticles());
        MinecraftTypes.writeList(buf, consumable.onConsumeEffects(), ItemTypes::writeConsumeEffect);
    }

    public static ConsumeEffect readConsumeEffect(ByteBuf buf) {
        return switch (MinecraftTypes.readVarInt(buf)) {
            case 0 -> new ConsumeEffect.ApplyEffects(MinecraftTypes.readList(buf, ItemTypes::readEffectInstance), buf.readFloat());
            case 1 -> new ConsumeEffect.RemoveEffects(MinecraftTypes.readHolderSet(buf));
            case 2 -> new ConsumeEffect.ClearAllEffects();
            case 3 -> new ConsumeEffect.TeleportRandomly(buf.readFloat());
            case 4 -> new ConsumeEffect.PlaySound(MinecraftTypes.readById(buf, BuiltinSound::from, MinecraftTypes::readSoundEvent));
            default -> throw new IllegalStateException("Unexpected value: " + MinecraftTypes.readVarInt(buf));
        };
    }

    public static void writeConsumeEffect(ByteBuf buf, ConsumeEffect consumeEffect) {
        if (consumeEffect instanceof ConsumeEffect.ApplyEffects applyEffects) {
            MinecraftTypes.writeVarInt(buf, 0);
            MinecraftTypes.writeList(buf, applyEffects.effects(), ItemTypes::writeEffectInstance);
            buf.writeFloat(applyEffects.probability());
        } else if (consumeEffect instanceof ConsumeEffect.RemoveEffects removeEffects) {
            MinecraftTypes.writeVarInt(buf, 1);
            MinecraftTypes.writeHolderSet(buf, removeEffects.effects());
        } else if (consumeEffect instanceof ConsumeEffect.ClearAllEffects) {
            MinecraftTypes.writeVarInt(buf, 2);
        } else if (consumeEffect instanceof ConsumeEffect.TeleportRandomly teleportRandomly) {
            MinecraftTypes.writeVarInt(buf, 3);
            buf.writeFloat(teleportRandomly.diameter());
        } else if (consumeEffect instanceof ConsumeEffect.PlaySound playSound) {
            MinecraftTypes.writeVarInt(buf, 4);
            if (playSound.sound() instanceof CustomSound) {
                MinecraftTypes.writeVarInt(buf, 0);
                MinecraftTypes.writeSoundEvent(buf, playSound.sound());
            } else {
                MinecraftTypes.writeVarInt(buf, ((BuiltinSound) playSound.sound()).ordinal() + 1);
            }
        }
    }

    public static UseCooldown readUseCooldown(ByteBuf buf) {
        return new UseCooldown(buf.readFloat(), MinecraftTypes.readNullable(buf, MinecraftTypes::readResourceLocation));
    }

    public static void writeUseCooldown(ByteBuf buf, UseCooldown useCooldown) {
        buf.writeFloat(useCooldown.seconds());
        MinecraftTypes.writeNullable(buf, useCooldown.cooldownGroup(), MinecraftTypes::writeResourceLocation);
    }

    public static MobEffectInstance readEffectInstance(ByteBuf buf) {
        Effect effect = MinecraftTypes.readEffect(buf);
        return new MobEffectInstance(effect, ItemTypes.readEffectDetails(buf));
    }

    public static MobEffectDetails readEffectDetails(ByteBuf buf) {
        int amplifier = MinecraftTypes.readVarInt(buf);
        int duration = MinecraftTypes.readVarInt(buf);
        boolean ambient = buf.readBoolean();
        boolean showParticles = buf.readBoolean();
        boolean showIcon = buf.readBoolean();
        MobEffectDetails hiddenEffect = MinecraftTypes.readNullable(buf, ItemTypes::readEffectDetails);
        return new MobEffectDetails(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
    }

    public static void writeEffectInstance(ByteBuf buf, MobEffectInstance instance) {
        MinecraftTypes.writeEffect(buf, instance.getEffect());
        ItemTypes.writeEffectDetails(buf, instance.getDetails());
    }

    public static void writeEffectDetails(ByteBuf buf, MobEffectDetails details) {
        MinecraftTypes.writeVarInt(buf, details.getAmplifier());
        MinecraftTypes.writeVarInt(buf, details.getDuration());
        buf.writeBoolean(details.isAmbient());
        buf.writeBoolean(details.isShowParticles());
        buf.writeBoolean(details.isShowIcon());
        MinecraftTypes.writeNullable(buf, details.getHiddenEffect(), ItemTypes::writeEffectDetails);
    }

    public static SuspiciousStewEffect readStewEffect(ByteBuf buf) {
        return new SuspiciousStewEffect(MinecraftTypes.readVarInt(buf), MinecraftTypes.readVarInt(buf));
    }

    public static void writeStewEffect(ByteBuf buf, SuspiciousStewEffect effect) {
        MinecraftTypes.writeVarInt(buf, effect.getMobEffectId());
        MinecraftTypes.writeVarInt(buf, effect.getDuration());
    }

    public static WritableBookContent readWritableBookContent(ByteBuf buf) {
        List<Filterable<String>> pages = MinecraftTypes.readList(buf, (input) -> ItemTypes.readFilterable(input, MinecraftTypes::readString));
        return new WritableBookContent(pages);
    }

    public static void writeWritableBookContent(ByteBuf buf, WritableBookContent content) {
        MinecraftTypes.writeList(buf, content.getPages(), (output, page) -> ItemTypes.writeFilterable(output, page, MinecraftTypes::writeString));
    }

    public static WrittenBookContent readWrittenBookContent(ByteBuf buf) {
        Filterable<String> title = ItemTypes.readFilterable(buf, MinecraftTypes::readString);
        String author = MinecraftTypes.readString(buf);
        int generation = MinecraftTypes.readVarInt(buf);

        List<Filterable<Component>> pages = MinecraftTypes.readList(buf, (input) -> ItemTypes.readFilterable(input, MinecraftTypes::readComponent));
        boolean resolved = buf.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public static void writeWrittenBookContent(ByteBuf buf, WrittenBookContent content) {
        ItemTypes.writeFilterable(buf, content.getTitle(), MinecraftTypes::writeString);
        MinecraftTypes.writeString(buf, content.getAuthor());
        MinecraftTypes.writeVarInt(buf, content.getGeneration());

        MinecraftTypes.writeList(buf, content.getPages(), (output, page) -> ItemTypes.writeFilterable(output, page, MinecraftTypes::writeComponent));

        buf.writeBoolean(content.isResolved());
    }

    public static ArmorTrim readArmorTrim(ByteBuf buf) {
        Holder<ArmorTrim.TrimMaterial> material = MinecraftTypes.readHolder(buf, ItemTypes::readTrimMaterial);
        Holder<ArmorTrim.TrimPattern> pattern = MinecraftTypes.readHolder(buf, ItemTypes::readTrimPattern);
        return new ArmorTrim(material, pattern);
    }

    public static void writeArmorTrim(ByteBuf buf, ArmorTrim trim) {
        MinecraftTypes.writeHolder(buf, trim.material(), ItemTypes::writeTrimMaterial);
        MinecraftTypes.writeHolder(buf, trim.pattern(), ItemTypes::writeTrimPattern);
    }

    public static ArmorTrim.TrimMaterial readTrimMaterial(ByteBuf buf) {
        String assetBase = MinecraftTypes.readString(buf);

        Map<Key, String> assetOverrides = new HashMap<>();
        int overrideCount = MinecraftTypes.readVarInt(buf);
        for (int i = 0; i < overrideCount; i++) {
            assetOverrides.put(MinecraftTypes.readResourceLocation(buf), MinecraftTypes.readString(buf));
        }

        Component description = MinecraftTypes.readComponent(buf);
        return new ArmorTrim.TrimMaterial(assetBase, assetOverrides, description);
    }

    public static void writeTrimMaterial(ByteBuf buf, ArmorTrim.TrimMaterial material) {
        MinecraftTypes.writeString(buf, material.assetBase());

        MinecraftTypes.writeVarInt(buf, material.assetOverrides().size());
        for (Map.Entry<Key, String> entry : material.assetOverrides().entrySet()) {
            MinecraftTypes.writeResourceLocation(buf, entry.getKey());
            MinecraftTypes.writeString(buf, entry.getValue());
        }

        MinecraftTypes.writeComponent(buf, material.description());
    }

    public static ArmorTrim.TrimPattern readTrimPattern(ByteBuf buf) {
        Key assetId = MinecraftTypes.readResourceLocation(buf);
        Component description = MinecraftTypes.readComponent(buf);
        boolean decal = buf.readBoolean();
        return new ArmorTrim.TrimPattern(assetId, description, decal);
    }

    public static void writeTrimPattern(ByteBuf buf, ArmorTrim.TrimPattern pattern) {
        MinecraftTypes.writeResourceLocation(buf, pattern.assetId());
        MinecraftTypes.writeComponent(buf, pattern.description());
        buf.writeBoolean(pattern.decal());
    }

    public static InstrumentComponent readInstrumentComponent(ByteBuf buf) {
        Holder<InstrumentComponent.Instrument> instrumentHolder = null;
        Key instrumentLocation = null;
        if (buf.readBoolean()) {
            instrumentHolder = MinecraftTypes.readHolder(buf, ItemTypes::readInstrument);
        } else {
            instrumentLocation = MinecraftTypes.readResourceLocation(buf);
        }
        return new InstrumentComponent(instrumentHolder, instrumentLocation);
    }

    public static void writeInstrumentComponent(ByteBuf buf, InstrumentComponent instrumentComponent) {
        buf.writeBoolean(instrumentComponent.instrumentHolder() != null);
        if (instrumentComponent.instrumentHolder() != null) {
            MinecraftTypes.writeHolder(buf, instrumentComponent.instrumentHolder(), ItemTypes::writeInstrument);
        } else {
            MinecraftTypes.writeResourceLocation(buf, instrumentComponent.instrumentLocation());
        }
    }

    public static InstrumentComponent.Instrument readInstrument(ByteBuf buf) {
        Sound soundEvent = MinecraftTypes.readSound(buf);
        float useDuration = buf.readFloat();
        float range = buf.readFloat();
        Component description = MinecraftTypes.readComponent(buf);
        return new InstrumentComponent.Instrument(soundEvent, useDuration, range, description);
    }

    public static void writeInstrument(ByteBuf buf, InstrumentComponent.Instrument instrument) {
        MinecraftTypes.writeSound(buf, instrument.soundEvent());
        buf.writeFloat(instrument.useDuration());
        buf.writeFloat(instrument.range());
        MinecraftTypes.writeComponent(buf, instrument.description());
    }

    public static ProvidesTrimMaterial readProvidesTrimMaterial(ByteBuf buf) {
        Holder<ArmorTrim.TrimMaterial> instrumentHolder = null;
        Key instrumentLocation = null;
        if (buf.readBoolean()) {
            instrumentHolder = MinecraftTypes.readHolder(buf, ItemTypes::readTrimMaterial);
        } else {
            instrumentLocation = MinecraftTypes.readResourceLocation(buf);
        }
        return new ProvidesTrimMaterial(instrumentHolder, instrumentLocation);
    }

    public static void writeProvidesTrimMaterial(ByteBuf buf, ProvidesTrimMaterial trimMaterial) {
        buf.writeBoolean(trimMaterial.materialHolder() != null);
        if (trimMaterial.materialHolder() != null) {
            MinecraftTypes.writeHolder(buf, trimMaterial.materialHolder(), ItemTypes::writeTrimMaterial);
        } else {
            MinecraftTypes.writeResourceLocation(buf, trimMaterial.materialLocation());
        }
    }

    public static NbtList<?> readRecipes(ByteBuf buf) {
        return MinecraftTypes.readAnyTag(buf, NbtType.LIST);
    }

    public static void writeRecipes(ByteBuf buf, NbtList<?> recipes) {
        MinecraftTypes.writeAnyTag(buf, recipes);
    }

    public static JukeboxPlayable readJukeboxPlayable(ByteBuf buf) {
        Holder<JukeboxPlayable.JukeboxSong> songHolder = null;
        Key songLocation = null;
        if (buf.readBoolean()) {
            songHolder = MinecraftTypes.readHolder(buf, ItemTypes::readJukeboxSong);
        } else {
            songLocation = MinecraftTypes.readResourceLocation(buf);
        }
        boolean showInTooltip = buf.readBoolean();
        return new JukeboxPlayable(songHolder, songLocation, showInTooltip);
    }

    public static void writeJukeboxPlayable(ByteBuf buf, JukeboxPlayable playable) {
        buf.writeBoolean(playable.songHolder() != null);
        if (playable.songHolder() != null) {
            MinecraftTypes.writeHolder(buf, playable.songHolder(), ItemTypes::writeJukeboxSong);
        } else {
            MinecraftTypes.writeResourceLocation(buf, playable.songLocation());
        }
        buf.writeBoolean(playable.showInTooltip());
    }

    public static JukeboxPlayable.JukeboxSong readJukeboxSong(ByteBuf buf) {
        Sound soundEvent = MinecraftTypes.readSound(buf);
        Component description = MinecraftTypes.readComponent(buf);
        float lengthInSeconds = buf.readFloat();
        int comparatorOutput = MinecraftTypes.readVarInt(buf);
        return new JukeboxPlayable.JukeboxSong(soundEvent, description, lengthInSeconds, comparatorOutput);
    }

    public static void writeJukeboxSong(ByteBuf buf, JukeboxPlayable.JukeboxSong song) {
        MinecraftTypes.writeSound(buf, song.soundEvent());
        MinecraftTypes.writeComponent(buf, song.description());
        buf.writeFloat(song.lengthInSeconds());
        MinecraftTypes.writeVarInt(buf, song.comparatorOutput());
    }

    public static LodestoneTracker readLodestoneTarget(ByteBuf buf) {
        return new LodestoneTracker(MinecraftTypes.readNullable(buf, MinecraftTypes::readGlobalPos), buf.readBoolean());
    }

    public static void writeLodestoneTarget(ByteBuf buf, LodestoneTracker target) {
        MinecraftTypes.writeNullable(buf, target.getPos(), MinecraftTypes::writeGlobalPos);
        buf.writeBoolean(target.isTracked());
    }

    public static Fireworks readFireworks(ByteBuf buf) {
        int flightDuration = MinecraftTypes.readVarInt(buf);

        List<Fireworks.FireworkExplosion> explosions = new ArrayList<>();
        int explosionCount = MinecraftTypes.readVarInt(buf);
        for (int i = 0; i < explosionCount; i++) {
            explosions.add(ItemTypes.readFireworkExplosion(buf));
        }

        return new Fireworks(flightDuration, explosions);
    }

    public static void writeFireworks(ByteBuf buf, Fireworks fireworks) {
        MinecraftTypes.writeVarInt(buf, fireworks.getFlightDuration());

        MinecraftTypes.writeVarInt(buf, fireworks.getExplosions().size());
        for (Fireworks.FireworkExplosion explosion : fireworks.getExplosions()) {
            ItemTypes.writeFireworkExplosion(buf, explosion);
        }
    }

    public static Fireworks.FireworkExplosion readFireworkExplosion(ByteBuf buf) {
        int shapeId = MinecraftTypes.readVarInt(buf);

        int[] colors = new int[MinecraftTypes.readVarInt(buf)];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = buf.readInt();
        }

        int[] fadeColors = new int[MinecraftTypes.readVarInt(buf)];
        for (int i = 0; i < fadeColors.length; i++) {
            fadeColors[i] = buf.readInt();
        }

        boolean hasTrail = buf.readBoolean();
        boolean hasTwinkle = buf.readBoolean();
        return new Fireworks.FireworkExplosion(shapeId, colors, fadeColors, hasTrail, hasTwinkle);
    }

    public static void writeFireworkExplosion(ByteBuf buf, Fireworks.FireworkExplosion explosion) {
        MinecraftTypes.writeVarInt(buf, explosion.getShapeId());

        MinecraftTypes.writeVarInt(buf, explosion.getColors().length);
        for (int color : explosion.getColors()) {
            buf.writeInt(color);
        }

        MinecraftTypes.writeVarInt(buf, explosion.getFadeColors().length);
        for (int fadeColor : explosion.getFadeColors()) {
            buf.writeInt(fadeColor);
        }

        buf.writeBoolean(explosion.isHasTrail());
        buf.writeBoolean(explosion.isHasTwinkle());
    }

    public static GameProfile readResolvableProfile(ByteBuf buf) {
        String name = MinecraftTypes.readNullable(buf, MinecraftTypes::readString);
        UUID id = MinecraftTypes.readNullable(buf, MinecraftTypes::readUUID);
        GameProfile profile = new GameProfile(id, name);

        List<GameProfile.Property> properties = MinecraftTypes.readList(buf, MinecraftTypes::readProperty);
        profile.setProperties(properties);

        return profile;
    }

    public static void writeResolvableProfile(ByteBuf buf, GameProfile profile) {
        MinecraftTypes.writeNullable(buf, profile.getName(), MinecraftTypes::writeString);
        MinecraftTypes.writeNullable(buf, profile.getId(), MinecraftTypes::writeUUID);

        MinecraftTypes.writeList(buf, profile.getProperties(), MinecraftTypes::writeProperty);
    }

    public static BannerPatternLayer readBannerPatternLayer(ByteBuf buf) {
        return new BannerPatternLayer(MinecraftTypes.readHolder(buf, ItemTypes::readBannerPattern), MinecraftTypes.readVarInt(buf));
    }

    public static void writeBannerPatternLayer(ByteBuf buf, BannerPatternLayer patternLayer) {
        MinecraftTypes.writeHolder(buf, patternLayer.getPattern(), ItemTypes::writeBannerPattern);
        MinecraftTypes.writeVarInt(buf, patternLayer.getColorId());
    }

    public static BannerPatternLayer.BannerPattern readBannerPattern(ByteBuf buf) {
        return new BannerPatternLayer.BannerPattern(MinecraftTypes.readResourceLocation(buf), MinecraftTypes.readString(buf));
    }

    public static void writeBannerPattern(ByteBuf buf, BannerPatternLayer.BannerPattern pattern) {
        MinecraftTypes.writeResourceLocation(buf, pattern.getAssetId());
        MinecraftTypes.writeString(buf, pattern.getTranslationKey());
    }

    public static BlockStateProperties readBlockStateProperties(ByteBuf buf) {
        Map<String, String> properties = new HashMap<>();
        int propertyCount = MinecraftTypes.readVarInt(buf);
        for (int i = 0; i < propertyCount; i++) {
            properties.put(MinecraftTypes.readString(buf), MinecraftTypes.readString(buf));
        }

        return new BlockStateProperties(properties);
    }

    public static void writeBlockStateProperties(ByteBuf buf, BlockStateProperties props) {
        MinecraftTypes.writeVarInt(buf, props.getProperties().size());
        for (Map.Entry<String, String> prop : props.getProperties().entrySet()) {
            MinecraftTypes.writeString(buf, prop.getKey());
            MinecraftTypes.writeString(buf, prop.getValue());
        }
    }

    public static BeehiveOccupant readBeehiveOccupant(ByteBuf buf) {
        return new BeehiveOccupant(MinecraftTypes.readCompoundTag(buf), MinecraftTypes.readVarInt(buf), MinecraftTypes.readVarInt(buf));
    }

    public static void writeBeehiveOccupant(ByteBuf buf, BeehiveOccupant occupant) {
        MinecraftTypes.writeAnyTag(buf, occupant.getEntityData());
        MinecraftTypes.writeVarInt(buf, occupant.getTicksInHive());
        MinecraftTypes.writeVarInt(buf, occupant.getMinTicksInHive());
    }
}
