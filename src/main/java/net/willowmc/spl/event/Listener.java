package net.willowmc.spl.event;

import org.bukkit.plugin.Plugin;

/**
 * Event Listener class for spl spigot events.
 */
public abstract class Listener implements org.bukkit.event.Listener {
    protected Plugin plugin;

    public void init(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
