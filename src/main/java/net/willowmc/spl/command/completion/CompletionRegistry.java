package net.willowmc.spl.command.completion;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Contains tab completions.
 */
@UtilityClass
public class CompletionRegistry {
    private final Map<String, Function<Player, List<String>>> completions = new HashMap<>();

    /**
     * Register a tab completion.
     *
     * @param completion placeholder
     * @param completer  completion
     * @return registered successfully
     */
    public boolean register(String completion, Function<Player, List<String>> completer) {
        if (completions.containsKey(completion)) return false;
        completions.put(completion, completer);
        return true;
    }

    /**
     * Register a tab completion.
     *
     * @param completion placeholder
     * @param completer  completion
     * @return registered successfully
     */
    public boolean registerDouble(String completion, Function<Player, Double> completer) {
        return register(completion, player -> List.of("" + completer.apply(player)));
    }

    /**
     * Register a tab completion.
     *
     * @param completion placeholder
     * @param completer  completion
     * @return registered successfully
     */
    public boolean registerInt(String completion, Function<Player, Integer> completer) {
        return register(completion, player -> List.of("" + completer.apply(player)));
    }

    /**
     * Register a tab completion.
     *
     * @param completion placeholder
     * @param completer  completion
     * @return registered successfully
     */
    public boolean registerString(String completion, Function<Player, String> completer) {
        return register(completion, player -> List.of(completer.apply(player)));
    }

    /**
     * Register a tab completion of the given enum.
     *
     * @param completion placeholder
     * @param e          enum
     * @return registered successfully
     */
    public boolean registerEnum(String completion, Class<? extends Enum<?>> e) {
        return register(completion, player -> Arrays.stream(e.getEnumConstants()).map(Enum::name).toList());
    }

    /**
     * Execute completion for given placeholder.
     *
     * @param completion placeholder
     * @param player     player
     * @return completion for placeholder
     */
    public List<String> complete(String completion, Player player) {
        if (completions.containsKey(completion)) return completions.get(completion).apply(player);
        return List.of(completion);
    }
}
