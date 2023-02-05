package net.mcaurora.spl.command;

import lombok.Getter;
import lombok.Setter;
import net.mcaurora.spl.SimplePluginLibrary;
import net.mcaurora.spl.command.exception.DuplicateCommandException;
import net.mcaurora.spl.command.exception.HookCommandException;
import net.mcaurora.spl.util.EnumHelper;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for SPL Commands.
 */
public abstract class Command {
    private final CommandData commandData;
    private final Subcommand[] subcommands;
    @Getter
    @Setter
    private BukkitCommand bukkitCommand;

    public Command() {
        this.commandData = this.getClass().getAnnotation(CommandData.class);
        this.subcommands = declareSubcommands();
    }

    public String getName() {
        return this.commandData.name();
    }

    public String getDescription() {
        return this.commandData.description();
    }

    public String[] getAliases() {
        return this.commandData.aliases();
    }

    public String getUsage() {
        return "<command> " + this.commandData.completion();
    }

    public String getCompletions() {
        return this.commandData.completion();
    }

    public boolean getPlayerOnly() {
        return this.commandData.playerOnly();
    }

    /**
     * Executed after the command gets registered.
     *
     * @param lib this instance
     * @throws HookCommandException
     * @throws DuplicateCommandException
     */
    public void postRegister(SimplePluginLibrary lib) throws HookCommandException, DuplicateCommandException {
        for (Subcommand subcommand : this.subcommands) {
            subcommand.register(getName(), 1, lib);
        }
        lib.getPerm().register(getName(), "Access to the " + getName() + " command.");
        PluginCommand command = lib.getPlugin().getCommand(getName());
        if (command == null) throw new HookCommandException(this);
        command.setExecutor(bukkitCommand);
    }

    /**
     * Executed when the command gets registered.
     */
    public void register() {
        if (!CommandRegistry.register(this)) throw new DuplicateCommandException(this);
    }

    /**
     * Get the command tab completions.
     *
     * @param ctx   command context
     * @param depth completion depth
     * @return tab completions (empty list if none)
     */
    @NotNull
    public List<String> getCompletions(CommandContext ctx, int depth) {
        List<String> completions;
        if (depth == 1) {
            completions = Arrays.stream(subcommands).map(Subcommand::getName).toList();
        } else {
            completions = new ArrayList<>();
            for (Subcommand subcommand : subcommands) {
                List<String> subCompletions = subcommand.doCompletion(ctx, depth);
                if (subCompletions != null) completions.addAll(subCompletions);
            }
        }
        return completions;
    }

    /**
     * Execute the command.
     *
     * @param ctx command context
     * @return successful execution
     */
    public boolean execute(CommandContext ctx) {
        for (Subcommand subcommand : subcommands) {
            if (ctx.args().length > 0 && ctx.args()[0].equals(subcommand.getName())) {
                return subcommand.execute(ctx);
            }
        }
        return doCommand(ctx);
    }

    /**
     * Check if a subcommand is registered.
     *
     * @param name command name
     * @return if the command exists
     */
    public boolean hasSubcommand(String name) {
        for (Subcommand subcommand : subcommands) {
            if (subcommand.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Override to declare subcommands.
     *
     * @return declared subcommands
     */
    protected Subcommand[] declareSubcommands() {
        return new Subcommand[0];
    }

    /**
     * Command Logic
     *
     * @param ctx command context
     * @return successful execution
     */
    protected abstract boolean doCommand(CommandContext ctx);

    /**
     * Get a command argument.
     *
     * @param ctx   command context
     * @param index index of argument
     * @return argument value (null if none)
     */
    public static String getArg(CommandContext ctx, int index) {
        if (ctx.args().length < index) return null;
        return ctx.args()[index];
    }

    /**
     * Get a command argument as a double.
     *
     * @param ctx   command context
     * @param index index of argument
     * @return argument value (null if none)
     */
    public static Double getDoubleArg(CommandContext ctx, int index) {
        try {
            String arg = getArg(ctx, index);
            if (arg != null) return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            // ignored
        }
        return null;
    }

    /**
     * Get a command argument as an integer.
     *
     * @param ctx   command context
     * @param index index of argument
     * @return argument value (null if none)
     */
    public static Integer getIntArg(CommandContext ctx, int index) {
        Double d = getDoubleArg(ctx, index);
        return d == null ? null : d.intValue();
    }

    /**
     * Get a command argument as the given enum.
     *
     * @param ctx   command context
     * @param index index of argument
     * @param type  enum class
     * @return argument value (null if none)
     */
    public static <E extends Enum<E>> E getEnumArg(CommandContext ctx, int index, Class<E> type) {
        String arg = getArg(ctx, index);
        if (arg != null && EnumHelper.isValid(arg, type)) {
            return Enum.valueOf(type, arg);
        }
        return null;
    }
}
