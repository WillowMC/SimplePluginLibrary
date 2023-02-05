package net.mcaurora.spl.command.completion;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Parses tab completion attempts based on arguments.
 */
@UtilityClass
public class TabCompletionParser {
    /**
     * Attempt to complete a command.
     *
     * @param sender Command Sender
     * @param args   Number of args
     * @param types  Tab Completion Arguments
     * @param offset Subcommand Depth
     * @return Completions (null if none)
     */
    @Nullable
    public List<String> complete(CommandSender sender, int args, String types, int offset) {
        if (types.length() == 0 || !(sender instanceof Player)) return null;
        String[] typeSplit = types.split(" ");
        int index = args - offset;
        if (typeSplit.length < index) return null;
        String completion = typeSplit[index - 1];
        return CompletionRegistry.complete(completion, ((Player) sender));
    }

    /**
     * Attempt to complete a command.
     *
     * @param sender Command Sender
     * @param args   Number of args
     * @param types  Tab Completion Arguments
     * @return Completions (null if none)
     */
    @Nullable
    public List<String> complete(CommandSender sender, int args, String types) {
        return complete(sender, args, types, 0);
    }
}
