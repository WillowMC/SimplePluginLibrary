package net.willowmc.spl.config.exception;

import net.willowmc.spl.config.Config;

/**
 * Thrown if a config file could not be found.
 */
public class ConfigNotFoundException extends RuntimeException {
    public ConfigNotFoundException(Config config) {
        super("Config with name " + config.getName().toLowerCase() + ".yml not found in plugin resources.");
    }
}
