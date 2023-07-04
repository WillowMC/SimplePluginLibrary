package net.willowmc.spl.command.exception;

/**
 * Thrown if interactions with Bukkit hidden fields fails.
 */
public class BukkitReflectionException extends RuntimeException {
    public BukkitReflectionException() {
        super("Failed to access internal bukkit classes to hook commands.");
    }
}
