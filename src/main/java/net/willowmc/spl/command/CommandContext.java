package net.willowmc.spl.command;

import org.bukkit.command.CommandSender;

/**
 * Holds data about the command being executed.
 *
 * @param command command being executed
 * @param args    command arguments
 * @param sender  command sender
 */
public record CommandContext(Command command, String[] args, CommandSender sender) {
    /**
     * Respond to the command sender.
     *
     * @param message message to respond with.
     */
    public void respond(String message) {
        sender().sendMessage(message);
    }
}
