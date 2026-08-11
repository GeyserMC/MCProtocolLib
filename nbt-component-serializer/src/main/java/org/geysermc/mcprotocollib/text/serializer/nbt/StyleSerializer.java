package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

/**
 * Reads and writes the style fields of a component.
 *
 * <p>Vanilla's {@code Style.Serializer} is a map codec rather than a codec, so its fields sit in the
 * same compound as the component's own content fields instead of a nested one. Every field is
 * optional, and an absent field means "inherit from the parent" - never "off" - which is why nothing
 * is written for a style that does not set it.
 */
final class StyleSerializer {

    /**
     * The decorations and the field each one is written under. Pairing them here keeps the reader and
     * the writer from ever disagreeing about a name, and lets both walk the same list.
     */
    private static final DecorationField[] DECORATIONS = {
        new DecorationField(TextDecoration.BOLD, ComponentFields.BOLD),
        new DecorationField(TextDecoration.ITALIC, ComponentFields.ITALIC),
        new DecorationField(TextDecoration.UNDERLINED, ComponentFields.UNDERLINED),
        new DecorationField(TextDecoration.STRIKETHROUGH, ComponentFields.STRIKETHROUGH),
        new DecorationField(TextDecoration.OBFUSCATED, ComponentFields.OBFUSCATED),
    };

    private final HoverEventSerializer hoverEventSerializer;

    StyleSerializer(final NbtComponentSerializerImpl serializer) {
        this.hoverEventSerializer = new HoverEventSerializer(serializer);
    }

    Style deserialize(final NbtMap tag) {
        final Style.Builder builder = Style.style();

        final String color = Tags.getString(tag, ComponentFields.COLOR);
        if (color != null) {
            builder.color(TextColorSerializer.deserialize(color));
        }

        final Object shadowColor = tag.get(ComponentFields.SHADOW_COLOR);
        if (shadowColor != null) {
            builder.shadowColor(TextColorSerializer.deserializeShadow(shadowColor));
        }

        for (final DecorationField decoration : DECORATIONS) {
            final Object value = tag.get(decoration.field());
            if (value != null) {
                builder.decoration(decoration.decoration(), TextDecoration.State.byBoolean(Tags.readBoolean(value)));
            }
        }

        final String insertion = Tags.getString(tag, ComponentFields.INSERTION);
        if (insertion != null) {
            builder.insertion(insertion);
        }

        final String font = Tags.getString(tag, ComponentFields.FONT);
        if (font != null) {
            builder.font(Key.key(font));
        }

        final NbtMap clickEvent = Tags.getCompound(tag, ComponentFields.CLICK_EVENT);
        if (clickEvent != null) {
            builder.clickEvent(ClickEventSerializer.deserialize(clickEvent));
        }

        final NbtMap hoverEvent = Tags.getCompound(tag, ComponentFields.HOVER_EVENT);
        if (hoverEvent != null) {
            builder.hoverEvent(this.hoverEventSerializer.deserialize(hoverEvent));
        }

        return builder.build();
    }

    /**
     * Writes the fields this style sets into the component's own compound. An empty style contributes
     * no keys at all.
     */
    void serialize(final Style style, final NbtMapBuilder tag) {
        final TextColor color = style.color();
        if (color != null) {
            tag.putString(ComponentFields.COLOR, TextColorSerializer.serialize(color));
        }

        final ShadowColor shadowColor = style.shadowColor();
        if (shadowColor != null) {
            // Vanilla's ARGB_COLOR_CODEC reads a list of channels but only ever writes the packed form
            tag.putInt(ComponentFields.SHADOW_COLOR, TextColorSerializer.serializeShadow(shadowColor));
        }

        for (final DecorationField decoration : DECORATIONS) {
            final TextDecoration.State state = style.decoration(decoration.decoration());
            if (state != TextDecoration.State.NOT_SET) {
                tag.putByte(decoration.field(), Tags.writeBoolean(state == TextDecoration.State.TRUE));
            }
        }

        final String insertion = style.insertion();
        if (insertion != null) {
            tag.putString(ComponentFields.INSERTION, insertion);
        }

        final Key font = style.font();
        if (font != null) {
            tag.putString(ComponentFields.FONT, font.asString());
        }

        final ClickEvent<?> clickEvent = style.clickEvent();
        if (clickEvent != null) {
            tag.putCompound(ComponentFields.CLICK_EVENT, ClickEventSerializer.serialize(clickEvent));
        }

        final HoverEvent<?> hoverEvent = style.hoverEvent();
        if (hoverEvent != null) {
            tag.putCompound(ComponentFields.HOVER_EVENT, this.hoverEventSerializer.serialize(hoverEvent));
        }
    }

    private record DecorationField(TextDecoration decoration, String field) {
    }
}
