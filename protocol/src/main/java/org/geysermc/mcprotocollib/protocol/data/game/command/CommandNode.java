package org.geysermc.mcprotocollib.protocol.data.game.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.CommandProperties;

import java.util.OptionalInt;

@Data
@AllArgsConstructor
public class CommandNode {
    /**
     * Type of command.
     */
    private final @NonNull CommandType type;

    /**
     * Whether the node is executable.
     */
    private final boolean executable;

    /**
     * Child node indices.
     */
    private final int @NonNull [] childIndices;

    /**
     * Redirect index, or empty if none is set.
     */
    private final OptionalInt redirectIndex;

    /**
     * Name of the node, if type is LITERAL or ARGUMENT.
     */
    private final String name;

    /**
     * Node parser, if type is ARGUMENT.
     */
    private final CommandParser parser;

    /**
     * Command properties, if type is ARGUMENT and the parser has properties.
     */
    private final CommandProperties properties;

    /**
     * Suggestions type, if present.
     * See {@link SuggestionType} for vanilla defaults.
     */
    private final String suggestionType;
}
