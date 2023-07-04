package net.willowmc.spl.command;

import net.willowmc.spl.SimplePluginLibrary;
import net.willowmc.spl.command.completion.TabCompletionParser;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command Executor for SPL Commands.
 */
public class BukkitCommand implements CommandExecutor, TabCompleter {
    private final net.willowmc.spl.command.Command command;

    protected BukkitCommand(net.willowmc.spl.command.Command command) {
        this.command = command;
    }

    /**
     * Bukkit CommandExecutor
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (this.command.getPlayerOnly() && !(commandSender instanceof Player)) return true;
        return this.command.execute(new CommandContext(this.command, strings, commandSender));
    }

    /**
     * Bukkit TabCompleter
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (this.command.getPlayerOnly() && !(commandSender instanceof Player)) return null;
        List<String> completions = new ArrayList<>();
        if (strings.length > 0 && !this.command.hasSubcommand(strings[0])) {
            List<String> commandCompletion = TabCompletionParser.complete(commandSender, strings.length, this.command.getCompletions());
            if (commandCompletion != null) completions.addAll(commandCompletion);
        }
        completions.addAll(this.command.getCompletions(new CommandContext(this.command, strings, commandSender), strings.length));
        return completions;
    }

    /**
     * Create a command object to register in the Bukkit CommandMap.
     *
     * @param lib this instance
     * @return command object
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public PluginCommand createCommandObject(SimplePluginLibrary lib) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);
        PluginCommand cmd = constructor.newInstance(command.getName(), lib.getPlugin());
        cmd.setDescription(command.getDescription());
        cmd.setUsage(command.getUsage());
        cmd.setAliases(Arrays.asList(command.getAliases()));
        return cmd;
    }
}
