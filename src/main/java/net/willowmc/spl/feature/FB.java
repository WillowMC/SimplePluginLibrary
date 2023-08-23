package net.willowmc.spl.feature;

import lombok.Getter;
import net.willowmc.spl.command.Command;
import net.willowmc.spl.event.Listener;

/**
 * Feature Builder, represents a Feature with a possible command and/or listener.
 */
public class FB {
    @Getter
    private byte state = 0;
    private boolean cset;
    @Getter
    private Command command;
    private boolean lset;
    @Getter
    private Listener listener;

    /**
     * Add a command
     *
     * @param command command
     * @return fb instance
     */
    public FB command(Command command) {
        this.command = command;
        if (!cset) state += 1;
        cset = true;
        return this;
    }

    /**
     * Add a listener
     *
     * @param listener listener
     * @return fb instance
     */
    public FB listener(Listener listener) {
        this.listener = listener;
        if (!lset) state += 2;
        lset = true;
        return this;
    }

    /**
     * Build the feature
     *
     * @return feature
     */
    public Feature b() {
        return new Feature(state, command, listener);
    }
}
