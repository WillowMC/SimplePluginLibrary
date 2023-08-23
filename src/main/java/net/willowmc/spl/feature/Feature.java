package net.willowmc.spl.feature;

import net.willowmc.spl.command.Command;
import net.willowmc.spl.event.Listener;

/**
 * Plugin Feature, holds commands/listeners
 *
 * @param state    if the feature has a command (1), a listener (2), none (0) or both (3)
 * @param command  command
 * @param listener listener
 */
public record Feature(byte state, Command command, Listener listener) {
    /**
     * Test a bit in the state variable
     *
     * @param i index of the bit
     * @return state of the bit (set/not set)
     */
    public boolean test(int i) {
        return state << ~i < 0;
    }
}
