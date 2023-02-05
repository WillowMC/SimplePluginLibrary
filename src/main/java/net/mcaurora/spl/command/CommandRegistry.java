package net.mcaurora.spl.command;

import lombok.experimental.UtilityClass;

import java.util.HashMap;

/**
 * Contains all registered commands.
 */
@UtilityClass
public class CommandRegistry {
    private final HashMap<String, Command> commands = new HashMap<>();

    /**
     * Register a command.
     *
     * @param command command to register
     * @return if command could be registered
     */
    public boolean register(Command command) {
        if (commands.containsKey(command.getName())) return false;
        command.setBukkitCommand(new BukkitCommand(command));
        commands.put(command.getName(), command);
        return true;
    }
}
