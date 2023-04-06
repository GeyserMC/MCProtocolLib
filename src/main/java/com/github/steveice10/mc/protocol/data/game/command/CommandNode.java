package com.github.steveice10.mc.protocol.data.game.command;

import com.github.steveice10.mc.protocol.data.game.command.properties.CommandProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

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
    private final @NonNull int[] childIndices;

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
