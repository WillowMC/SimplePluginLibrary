package net.willowmc.spl.tool.exception;

/**
 * Thrown if a tool item is created, before the plugin and spl has been initialized.
 */
public class ToolItemInitException extends RuntimeException {
    public ToolItemInitException() {
        super("Tool item can not be created before SPL has been initialized.");
    }
}
