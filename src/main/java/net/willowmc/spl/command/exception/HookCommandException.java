package net.willowmc.spl.command.exception;

import net.willowmc.spl.command.Command;

/**
 * Thrown if a command can not be registered with Bukkit.
 */
public class HookCommandException extends RuntimeException {
    public HookCommandException(Command command) {
        super("Failed to hook command with name " + command.getName() + ".");
    }
}
