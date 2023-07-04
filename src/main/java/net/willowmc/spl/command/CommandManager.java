package net.willowmc.spl.command;

import net.willowmc.spl.SimplePluginLibrary;
import net.willowmc.spl.command.exception.BukkitReflectionException;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Handles command registration and loading.
 */
public class CommandManager {
    private final CommandMap commandMap;

    /**
     * @param commands list of commands to register
     * @param lib      this instance
     */
    public CommandManager(Command[] commands, SimplePluginLibrary lib) {
        try {
            commandMap = getCommandMap(lib);
            for (Command command : commands) {
                command.register();
                commandMap.register(lib.getPlugin().getName(), command.getBukkitCommand().createCommandObject(lib));
                command.postRegister(lib);
            }
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new BukkitReflectionException();
        }
    }

    /**
     * Get the CommandMap instance from Bukkit.
     *
     * @param lib this instance
     * @return CommandMap instance
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private CommandMap getCommandMap(SimplePluginLibrary lib) throws NoSuchFieldException, IllegalAccessException {
        Field mapField = SimplePluginManager.class.getDeclaredField("commandMap");
        mapField.setAccessible(true);
        return (CommandMap) (mapField.get(lib.getPlugin().getServer().getPluginManager()));
    }
}
