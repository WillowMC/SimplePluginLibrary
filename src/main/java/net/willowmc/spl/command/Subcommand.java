package net.willowmc.spl.command;

import lombok.Getter;
import net.willowmc.spl.SimplePluginLibrary;
import net.willowmc.spl.command.completion.TabCompletionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Subcommand that can be assigned to a command or to another subcommand.
 */
public class Subcommand {
    @Getter
    private final String name;
    private final String completion;
    private final Subcommand[] subcommands;
    private final Function<CommandContext, Boolean> executor;
    private int depth;

    /**
     * Create a new Subcommand.
     *
     * @param name        command name
     * @param completion  command completion
     * @param executor    command logic
     * @param subcommands subcommands
     */
    public Subcommand(String name, String completion, Function<CommandContext, Boolean> executor, Subcommand[] subcommands) {
        this.name = name;
        this.completion = completion;
        this.executor = executor;
        this.subcommands = subcommands;
    }

    /**
     * Create a new Subcommand.
     *
     * @param name       command name
     * @param completion command completion
     * @param executor   command logic
     */
    public Subcommand(String name, String completion, Function<CommandContext, Boolean> executor) {
        this(name, completion, executor, new Subcommand[0]);
    }

    /**
     * Create a new Subcommand.
     *
     * @param name        command name
     * @param executor    command logic
     * @param subcommands subcommands
     */
    public Subcommand(String name, Function<CommandContext, Boolean> executor, Subcommand[] subcommands) {
        this(name, "", executor, subcommands);
    }

    /**
     * Create a new Subcommand.
     *
     * @param name     command name
     * @param executor command logic
     */
    public Subcommand(String name, Function<CommandContext, Boolean> executor) {
        this(name, "", executor, new Subcommand[0]);
    }

    /**
     * Register the subcommand.
     *
     * @param parent parent command name
     * @param depth  depth of the subcommand
     * @param lib    this instance
     */
    public void register(String parent, int depth, SimplePluginLibrary lib) {
        this.depth = depth;
        for (Subcommand subcommand : subcommands) {
            subcommand.register(parent + "." + getName(), this.depth + 1, lib);
        }
        lib.getPerm().register(getName(), "Access to the " + parent.replaceAll("\\.", " ") + " " + getName() + "command.");
    }

    /**
     * Execute the subcommand logic.
     *
     * @param context command context
     * @return successful execution
     */
    public boolean execute(CommandContext context) {
        if (!context.getPlayer().hasPermission(this.name)) return true;
        for (Subcommand subcommand : subcommands) {
            if (context.args().length > depth && context.args()[depth].equals(subcommand.name)) {
                return subcommand.execute(context);
            }
        }
        return executor.apply(context);
    }

    /**
     * Do tab completion.
     *
     * @param ctx   command context
     * @param depth depth of the completion
     * @return tab completions (nullable)
     */
    public List<String> doCompletion(CommandContext ctx, int depth) {
        if (depth >= this.depth && ctx.args()[this.depth - 1].equals(getName())) {
            if (!ctx.getPlayer().hasPermission(this.name)) return null;
            if (!hasSubcommand(ctx.args()[this.depth])) {
                List<String> tabCompletion = new ArrayList<>();
                if (depth == this.depth + 1) {
                    for (Subcommand subcommand : subcommands) {
                        tabCompletion.add(subcommand.name);
                    }
                }
                List<String> argsCompletion = TabCompletionParser.complete(ctx.sender(), depth, this.completion, this.depth);
                if (argsCompletion != null) tabCompletion.addAll(argsCompletion);
                return tabCompletion;
            } else {
                for (Subcommand subcommand : subcommands) {
                    if (depth > this.depth && ctx.args()[this.depth].equals(subcommand.name))
                        return subcommand.doCompletion(ctx, depth);
                }
            }
        }
        return null;
    }

    /**
     * Check if a subcommand is registered.
     *
     * @param name name of subcommand
     * @return does the command exist
     */
    public boolean hasSubcommand(String name) {
        for (Subcommand subcommand : subcommands) {
            if (subcommand.getName().equals(name)) return true;
        }
        return false;
    }
}
