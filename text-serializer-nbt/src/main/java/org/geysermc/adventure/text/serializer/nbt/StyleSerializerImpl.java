package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

final class StyleSerializerImpl {

    static Style deserialize(NbtMap map, NbtComponentSerializer componentSerializer) {
        return Style.style(builder -> {
            map.listenForString("color", color -> {
                TextColor parsed = TextColor.fromHexString(color);
                if (parsed == null) {
                    parsed = NamedTextColor.NAMES.valueOrThrow(color);
                }
                builder.color(parsed);
            });
            Object shadowColor = map.get("shadow_color");
            if (shadowColor != null) {
                builder.shadowColor(NbtUtil.deserializeARGB(shadowColor));
            }

            map.listenForBoolean("bold", bold -> builder.decoration(TextDecoration.BOLD, bold));
            map.listenForBoolean("italic", bold -> builder.decoration(TextDecoration.ITALIC, bold));
            map.listenForBoolean("underlined", bold -> builder.decoration(TextDecoration.UNDERLINED, bold));
            map.listenForBoolean("strikethrough", bold -> builder.decoration(TextDecoration.STRIKETHROUGH, bold));
            map.listenForBoolean("obfuscated", bold -> builder.decoration(TextDecoration.OBFUSCATED, bold));

            map.listenForCompound("click_event", clickEvent -> builder.clickEvent(ClickEventSerializerImpl.deserialize(clickEvent)));
            map.listenForCompound("hover_event", hoverEvent -> builder.hoverEvent(HoverEventSerializerImpl.deserialize(hoverEvent, componentSerializer)));

            map.listenForString("insertion", builder::insertion);
            map.listenForString("font", font -> builder.font(Key.key(font)));
        });
    }

    static void serialize(NbtMapBuilder builder, Style style, NbtComponentSerializer componentSerializer) {
        NbtUtil.checkNonNull(style.color(), color -> {
            if (color instanceof NamedTextColor named) {
                builder.putString("color", named.name());
            } else {
                builder.putString("color", color.asHexString());
            }
        });
        NbtUtil.checkNonNull(style.shadowColor(), color -> builder.putInt("shadow_color", color.value()));

        style.decorations().forEach((decoration, state) -> {
            if (state != TextDecoration.State.NOT_SET) {
                builder.putBoolean(decoration.toString(), state == TextDecoration.State.TRUE);
            }
        });

        NbtUtil.checkNonNull(style.clickEvent(), clickEvent -> builder.putCompound("click_event", ClickEventSerializerImpl.serialize(clickEvent)));
        NbtUtil.checkNonNull(style.hoverEvent(), hoverEvent -> builder.putCompound("hover_event", HoverEventSerializerImpl.serialize(hoverEvent, componentSerializer)));

        NbtUtil.checkNonNull(style.insertion(), insertion -> builder.putString("insertion", insertion));
        NbtUtil.checkNonNull(style.font(), font -> builder.putString("font", font.asString()));
    }
}
