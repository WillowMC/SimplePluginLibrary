package net.mcaurora.spl.command.exception;

import net.mcaurora.spl.command.Command;

/**
 * Thrown if a registered command already exists.
 */
public class DuplicateCommandException extends RuntimeException {
    public DuplicateCommandException(Command command) {
        super("Command with name " + command.getName() + " already exists.");
    }
}
