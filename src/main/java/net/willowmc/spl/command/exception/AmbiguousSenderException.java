package net.willowmc.spl.command.exception;

import net.willowmc.spl.command.Command;

/**
 * Thrown if a command is not player only, but the player is retrieved.
 */
public class AmbiguousSenderException extends RuntimeException {
    public AmbiguousSenderException(Command command) {
        super("Sender for command " + command.getName() + " is not guaranteed to be a player.");
    }
}
