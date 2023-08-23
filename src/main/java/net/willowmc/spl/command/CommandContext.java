package net.willowmc.spl.command;

import net.willowmc.spl.command.exception.AmbiguousSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
     * @param message message to respond with
     */
    public void respond(String message) {
        sender().sendMessage(message);
    }

    /**
     * Returns the command sender as a player, throws if the command is not player only.
     *
     * @return command sender as player
     */
    public Player getPlayer() {
        if (command.getPlayerOnly()) return (Player) sender;
        throw new AmbiguousSenderException(command);
    }
}
