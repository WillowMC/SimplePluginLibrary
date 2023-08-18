package net.willowmc.spl.config.exception;

import net.willowmc.spl.config.Config;

/**
 * Thrown if a config file could not be written.
 */
public class ConfigReadWriteException extends RuntimeException {
    public ConfigReadWriteException(Config config) {
        super("Config with name " + config.getName().toLowerCase() + ".yml could not be written to or read from.");
    }
}
