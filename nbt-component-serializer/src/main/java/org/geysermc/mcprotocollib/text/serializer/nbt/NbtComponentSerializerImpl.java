package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.Style;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class NbtComponentSerializerImpl implements NbtComponentSerializer {

    static final NbtComponentSerializer INSTANCE = new NbtComponentSerializerImpl(false);

    private final boolean emitComponentType;
    private final StyleSerializer styleSerializer;
    private final TranslationArgumentSerializer translationArgumentSerializer;

    NbtComponentSerializerImpl(final boolean emitComponentType) {
        this.emitComponentType = emitComponentType;
        this.styleSerializer = new StyleSerializer(this);
        this.translationArgumentSerializer = new TranslationArgumentSerializer(this);
    }

    // Reading

    @Override
    public Component deserialize(final Object tag) {
        if (tag instanceof String text) {
            // A bare string is a plain text component with no style and no siblings
            return Component.text(text);
        } else if (tag instanceof NbtList<?> list) {
            return this.deserializeList(list);
        } else if (tag instanceof NbtMap compound) {
            return this.deserializeCompound(compound);
        }
        throw new NbtSerializationException("A component must be a string, a list or a compound, got " + Tags.typeNameOf(tag));
    }

    /**
     * The list shorthand: the first entry is the component, and the rest become its siblings. See
     * vanilla's {@code ComponentSerialization#createFromList}.
     */
    private Component deserializeList(final NbtList<?> list) {
        if (list.isEmpty()) {
            throw new NbtSerializationException("A component written as a list must not be empty");
        }

        final List<Component> entries = new ArrayList<>(list.size());
        for (final Object entry : list) {
            entries.add(this.deserialize(entry));
        }

        final Component first = entries.getFirst();
        return entries.size() == 1 ? first : first.children(concat(first.children(), entries.subList(1, entries.size())));
    }

    private Component deserializeCompound(final NbtMap tag) {
        final ComponentBuilder<?, ?> builder = this.deserializeContents(tag);
        builder.style(this.deserializeStyle(tag));

        for (final Object sibling : Tags.getList(tag, ComponentFields.EXTRA)) {
            builder.append(this.deserialize(sibling));
        }

        return builder.build();
    }

    /**
     * Picks the content type and reads its fields.
     *
     * <p>An explicit {@code type} wins, as it does in vanilla's {@code StrictEither}. Without one,
     * the type is whichever content's identifying field is present first, matching the order
     * vanilla's {@code FuzzyCodec} tries its content codecs in.
     */
    private ComponentBuilder<?, ?> deserializeContents(final NbtMap tag) {
        final String type = Tags.getString(tag, ComponentFields.TYPE);
        if (type != null) {
            return switch (type) {
                case ComponentFields.TYPE_TEXT -> this.deserializeText(tag);
                case ComponentFields.TYPE_TRANSLATABLE -> this.deserializeTranslatable(tag);
                case ComponentFields.TYPE_KEYBIND -> deserializeKeybind(tag);
                case ComponentFields.TYPE_SCORE -> deserializeScore(tag);
                case ComponentFields.TYPE_SELECTOR -> this.deserializeSelector(tag);
                case ComponentFields.TYPE_NBT -> this.deserializeNbt(tag);
                case ComponentFields.TYPE_OBJECT -> this.deserializeObject(tag);
                // An unknown discriminator is not ours to interpret; fall back to the field shape
                default -> this.deserializeContentsByFields(tag);
            };
        }

        return this.deserializeContentsByFields(tag);
    }

    private ComponentBuilder<?, ?> deserializeContentsByFields(final NbtMap tag) {
        if (tag.containsKey(ComponentFields.TEXT)) {
            return this.deserializeText(tag);
        } else if (tag.containsKey(ComponentFields.TRANSLATE)) {
            return this.deserializeTranslatable(tag);
        } else if (tag.containsKey(ComponentFields.KEYBIND)) {
            return deserializeKeybind(tag);
        } else if (tag.containsKey(ComponentFields.SCORE)) {
            return deserializeScore(tag);
        } else if (tag.containsKey(ComponentFields.SELECTOR)) {
            return this.deserializeSelector(tag);
        } else if (tag.containsKey(ComponentFields.NBT)) {
            return this.deserializeNbt(tag);
        } else if (tag.containsKey(ComponentFields.OBJECT)
            || tag.containsKey(ComponentFields.SPRITE)
            || tag.containsKey(ComponentFields.PLAYER)) {
            return this.deserializeObject(tag);
        }

        throw new NbtSerializationException("A component compound has no content fields: " + tag.keySet());
    }

    private TextComponent.Builder deserializeText(final NbtMap tag) {
        return Component.text().content(Tags.getRequiredString(tag, ComponentFields.TEXT));
    }

    private TranslatableComponent.Builder deserializeTranslatable(final NbtMap tag) {
        final TranslatableComponent.Builder builder = Component.translatable()
            .key(Tags.getRequiredString(tag, ComponentFields.TRANSLATE))
            .fallback(Tags.getString(tag, ComponentFields.TRANSLATE_FALLBACK));

        final List<Object> arguments = Tags.getList(tag, ComponentFields.TRANSLATE_WITH);
        if (!arguments.isEmpty()) {
            final List<TranslationArgument> converted = new ArrayList<>(arguments.size());
            for (final Object argument : arguments) {
                converted.add(this.translationArgumentSerializer.deserialize(argument));
            }
            builder.arguments(converted);
        }

        return builder;
    }

    private static KeybindComponent.Builder deserializeKeybind(final NbtMap tag) {
        return Component.keybind().keybind(Tags.getRequiredString(tag, ComponentFields.KEYBIND));
    }

    private static ScoreComponent.Builder deserializeScore(final NbtMap tag) {
        // The score fields sit one level down, in a "score" sub-compound
        final NbtMap score = Tags.getRequiredCompound(tag, ComponentFields.SCORE);
        return Component.score()
            .name(Tags.getRequiredString(score, ComponentFields.SCORE_NAME))
            .objective(Tags.getRequiredString(score, ComponentFields.SCORE_OBJECTIVE));
    }

    private SelectorComponent.Builder deserializeSelector(final NbtMap tag) {
        return Component.selector()
            .pattern(Tags.getRequiredString(tag, ComponentFields.SELECTOR))
            .separator(this.deserializeSeparator(tag));
    }

    private ComponentBuilder<?, ?> deserializeNbt(final NbtMap tag) {
        final String path = Tags.getRequiredString(tag, ComponentFields.NBT);
        final boolean interpret = Tags.getBooleanOrDefault(tag, ComponentFields.NBT_INTERPRET, false);
        final boolean plain = Tags.getBooleanOrDefault(tag, ComponentFields.NBT_PLAIN, false);
        if (interpret && plain) {
            throw new NbtSerializationException("An nbt component cannot be both interpreted and plain");
        }
        final Component separator = this.deserializeSeparator(tag);

        final NBTComponentBuilder<?, ?> builder;
        final String source = Tags.getString(tag, ComponentFields.NBT_SOURCE);
        if (equalsOrPresent(tag, source, ComponentFields.NBT_SOURCE_BLOCK)) {
            builder = Component.blockNBT()
                .pos(BlockNBTComponent.Pos.fromString(Tags.getRequiredString(tag, ComponentFields.NBT_SOURCE_BLOCK)));
        } else if (equalsOrPresent(tag, source, ComponentFields.NBT_SOURCE_ENTITY)) {
            builder = Component.entityNBT()
                .selector(Tags.getRequiredString(tag, ComponentFields.NBT_SOURCE_ENTITY));
        } else if (equalsOrPresent(tag, source, ComponentFields.NBT_SOURCE_STORAGE)) {
            builder = Component.storageNBT()
                .storage(Key.key(Tags.getRequiredString(tag, ComponentFields.NBT_SOURCE_STORAGE)));
        } else {
            throw new NbtSerializationException("An nbt component needs a block, entity or storage source: " + tag.keySet());
        }

        return builder.nbtPath(path).interpret(interpret).plain(plain).separator(separator);
    }

    /**
     * A data source is identified by an explicit {@code source} discriminator when there is one, and
     * otherwise by which of the source fields is present.
     */
    private static boolean equalsOrPresent(final NbtMap tag, final @Nullable String source, final String field) {
        return source != null ? source.equals(field) : tag.containsKey(field);
    }

    private ObjectComponent.Builder deserializeObject(final NbtMap tag) {
        return Component.object()
            .contents(ObjectContentsSerializer.deserialize(tag))
            .fallback(this.deserializeOptionalComponent(tag, ComponentFields.FALLBACK));
    }

    private @Nullable Component deserializeSeparator(final NbtMap tag) {
        return this.deserializeOptionalComponent(tag, ComponentFields.SEPARATOR);
    }

    @Nullable Component deserializeOptionalComponent(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        return value == null ? null : this.deserialize(value);
    }

    @Override
    public Style deserializeStyle(final NbtMap tag) {
        return this.styleSerializer.deserialize(tag);
    }

    // Writing

    @Override
    public Object serialize(final Component component) {
        // Vanilla's tryCollapseToString: a plain unstyled literal with no siblings is written as a
        // bare string rather than a single field compound
        if (component instanceof TextComponent text && text.children().isEmpty() && !text.hasStyling()) {
            return text.content();
        }
        return this.serializeToCompound(component);
    }

    NbtMap serializeToCompound(final Component component) {
        final NbtMapBuilder tag = NbtMap.builder();
        this.serializeContents(component, tag);
        this.styleSerializer.serialize(component.style(), tag);

        final List<Component> children = component.children();
        if (!children.isEmpty()) {
            final List<Object> siblings = new ArrayList<>(children.size());
            for (final Component child : children) {
                siblings.add(this.serialize(child));
            }
            tag.put(ComponentFields.EXTRA, Tags.buildList(siblings));
        }

        return tag.build();
    }

    private void serializeContents(final Component component, final NbtMapBuilder tag) {
        switch (component) {
            case TextComponent text -> {
                this.putType(tag, ComponentFields.TYPE_TEXT);
                tag.putString(ComponentFields.TEXT, text.content());
            }
            case TranslatableComponent translatable -> {
                this.putType(tag, ComponentFields.TYPE_TRANSLATABLE);
                tag.putString(ComponentFields.TRANSLATE, translatable.key());

                final String fallback = translatable.fallback();
                if (fallback != null) {
                    tag.putString(ComponentFields.TRANSLATE_FALLBACK, fallback);
                }

                final List<TranslationArgument> arguments = translatable.arguments();
                if (!arguments.isEmpty()) {
                    final List<Object> serialized = new ArrayList<>(arguments.size());
                    for (final TranslationArgument argument : arguments) {
                        serialized.add(this.translationArgumentSerializer.serialize(argument));
                    }
                    tag.put(ComponentFields.TRANSLATE_WITH, Tags.buildList(serialized));
                }
            }
            case KeybindComponent keybind -> {
                this.putType(tag, ComponentFields.TYPE_KEYBIND);
                tag.putString(ComponentFields.KEYBIND, keybind.keybind());
            }
            case ScoreComponent score -> {
                this.putType(tag, ComponentFields.TYPE_SCORE);
                tag.putCompound(ComponentFields.SCORE, NbtMap.builder()
                    .putString(ComponentFields.SCORE_NAME, score.name())
                    .putString(ComponentFields.SCORE_OBJECTIVE, score.objective())
                    .build());
            }
            case SelectorComponent selector -> {
                this.putType(tag, ComponentFields.TYPE_SELECTOR);
                tag.putString(ComponentFields.SELECTOR, selector.pattern());
                this.putOptionalComponent(tag, ComponentFields.SEPARATOR, selector.separator());
            }
            case NBTComponent<?> nbt -> {
                this.putType(tag, ComponentFields.TYPE_NBT);
                this.serializeNbtContents(nbt, tag);
            }
            case ObjectComponent object -> {
                this.putType(tag, ComponentFields.TYPE_OBJECT);
                ObjectContentsSerializer.serialize(object.contents(), tag, this.emitComponentType);
                this.putOptionalComponent(tag, ComponentFields.FALLBACK, object.fallback());
            }
            default -> throw new NbtSerializationException("Unknown component type " + component.getClass().getName());
        }
    }

    private void serializeNbtContents(final NBTComponent<?> nbt, final NbtMapBuilder tag) {
        tag.putString(ComponentFields.NBT, nbt.nbtPath());
        if (nbt.interpret()) {
            tag.putBoolean(ComponentFields.NBT_INTERPRET, true);
        }
        if (nbt.plain()) {
            tag.putBoolean(ComponentFields.NBT_PLAIN, true);
        }
        this.putOptionalComponent(tag, ComponentFields.SEPARATOR, nbt.separator());

        switch (nbt) {
            case BlockNBTComponent block -> tag.putString(ComponentFields.NBT_SOURCE_BLOCK, block.pos().asString());
            case EntityNBTComponent entity -> tag.putString(ComponentFields.NBT_SOURCE_ENTITY, entity.selector());
            case StorageNBTComponent storage -> tag.putString(ComponentFields.NBT_SOURCE_STORAGE, storage.storage().asString());
            default -> throw new NbtSerializationException("Unknown nbt component source " + nbt.getClass().getName());
        }
    }

    private void putOptionalComponent(final NbtMapBuilder tag, final String key, final @Nullable Component component) {
        if (component != null) {
            tag.put(key, this.serialize(component));
        }
    }

    /**
     * Writes the content type discriminator, if this serializer was asked to.
     */
    private void putType(final NbtMapBuilder tag, final String type) {
        if (this.emitComponentType) {
            tag.putString(ComponentFields.TYPE, type);
        }
    }

    @Override
    public NbtMap serializeStyle(final Style style) {
        final NbtMapBuilder tag = NbtMap.builder();
        this.styleSerializer.serialize(style, tag);
        return tag.build();
    }

    private static List<Component> concat(final List<Component> first, final List<Component> second) {
        final List<Component> combined = new ArrayList<>(first.size() + second.size());
        combined.addAll(first);
        combined.addAll(second);
        return combined;
    }

    static final class BuilderImpl implements Builder {
        private boolean emitComponentType;

        @Override
        public Builder emitComponentType(final boolean emitComponentType) {
            this.emitComponentType = emitComponentType;
            return this;
        }

        @Override
        public NbtComponentSerializer build() {
            return new NbtComponentSerializerImpl(this.emitComponentType);
        }
    }
}
