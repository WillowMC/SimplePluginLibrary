package net.willowmc.spl.config.exception;

/**
 * Throws if the config folder can not be found or accessed.
 */
public class ConfigFolderException extends RuntimeException {
    public ConfigFolderException() {
        super("Config folder could not be found, created or accessed.");
    }
}
