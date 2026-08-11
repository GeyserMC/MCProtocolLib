package org.geysermc.mcprotocollib.text.serializer.nbt;

/**
 * Every key name that appears in the nbt representation of a text component.
 *
 * <p>The names mirror vanilla's {@code ComponentSerialization}, {@code Style.Serializer},
 * {@code ClickEvent}, {@code HoverEvent} and the {@code ComponentContents} implementations. Keeping
 * them in one place means the serializer and the deserializer can never disagree on a spelling.
 */
final class ComponentFields {

    // Shared structure
    static final String TYPE = "type";
    static final String EXTRA = "extra";
    static final String SEPARATOR = "separator";
    static final String FALLBACK = "fallback";

    // Content type discriminators, in the order vanilla registers them in
    // ComponentSerialization#bootstrap. The fuzzy codec tries them in this order when no explicit
    // "type" is present, so field-presence detection has to walk them in the same order.
    static final String TYPE_TEXT = "text";
    static final String TYPE_TRANSLATABLE = "translatable";
    static final String TYPE_KEYBIND = "keybind";
    static final String TYPE_SCORE = "score";
    static final String TYPE_SELECTOR = "selector";
    static final String TYPE_NBT = "nbt";
    static final String TYPE_OBJECT = "object";

    // PlainTextContents
    static final String TEXT = "text";

    // TranslatableContents
    static final String TRANSLATE = "translate";
    static final String TRANSLATE_WITH = "with";
    static final String TRANSLATE_FALLBACK = "fallback";

    // KeybindContents
    static final String KEYBIND = "keybind";

    // ScoreContents - note that the fields live inside a "score" sub-compound
    static final String SCORE = "score";
    static final String SCORE_NAME = "name";
    static final String SCORE_OBJECTIVE = "objective";

    // SelectorContents
    static final String SELECTOR = "selector";

    // NbtContents, plus its DataSource variants
    static final String NBT = "nbt";
    static final String NBT_INTERPRET = "interpret";
    static final String NBT_PLAIN = "plain";
    static final String NBT_SOURCE = "source";
    static final String NBT_SOURCE_BLOCK = "block";
    static final String NBT_SOURCE_ENTITY = "entity";
    static final String NBT_SOURCE_STORAGE = "storage";

    // ObjectContents and its ObjectInfo variants
    static final String OBJECT = "object";
    static final String OBJECT_ATLAS_SPRITE = "atlas";
    static final String OBJECT_PLAYER_HEAD = "player";
    static final String SPRITE = "sprite";
    static final String ATLAS = "atlas";
    static final String PLAYER = "player";
    static final String HAT = "hat";

    // ResolvableProfile, of a player head object
    static final String PROFILE_NAME = "name";
    static final String PROFILE_ID = "id";
    static final String PROFILE_PROPERTIES = "properties";
    static final String PROFILE_PROPERTY_NAME = "name";
    static final String PROFILE_PROPERTY_VALUE = "value";
    static final String PROFILE_PROPERTY_SIGNATURE = "signature";

    // Style
    static final String COLOR = "color";
    static final String SHADOW_COLOR = "shadow_color";
    static final String FONT = "font";
    static final String INSERTION = "insertion";
    static final String BOLD = "bold";
    static final String ITALIC = "italic";
    static final String UNDERLINED = "underlined";
    static final String STRIKETHROUGH = "strikethrough";
    static final String OBFUSCATED = "obfuscated";
    static final String CLICK_EVENT = "click_event";
    static final String HOVER_EVENT = "hover_event";

    // ClickEvent
    static final String ACTION = "action";
    static final String CLICK_URL = "url";
    static final String CLICK_PATH = "path";
    static final String CLICK_COMMAND = "command";
    static final String CLICK_PAGE = "page";
    static final String CLICK_VALUE = "value";
    static final String CLICK_DIALOG = "dialog";
    static final String CLICK_ID = "id";
    static final String CLICK_PAYLOAD = "payload";

    // HoverEvent
    static final String HOVER_SHOW_TEXT_VALUE = "value";
    static final String HOVER_ITEM_ID = "id";
    static final String HOVER_ITEM_COUNT = "count";
    static final String HOVER_ITEM_COMPONENTS = "components";
    static final String HOVER_ENTITY_ID = "id";
    static final String HOVER_ENTITY_UUID = "uuid";
    static final String HOVER_ENTITY_NAME = "name";

    private ComponentFields() {
    }
}
