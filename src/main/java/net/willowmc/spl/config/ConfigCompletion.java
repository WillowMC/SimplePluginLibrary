package net.willowmc.spl.config;

import java.util.function.Function;

/**
 * Wrapper for completions for config files.
 *
 * @param name     name of the placeholder in the config file
 * @param function function that returns a string to replace the placeholder
 */
public record ConfigCompletion(String name, Function<String, String> function) {
}
