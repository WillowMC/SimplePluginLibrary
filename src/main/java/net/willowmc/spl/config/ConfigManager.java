package net.willowmc.spl.config;

import net.willowmc.spl.config.exception.ConfigFolderException;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds and manages all created configs.
 */
public class ConfigManager {
    private final Plugin plugin;
    private final Map<String, Config> configs;

    /**
     * Create the Config Manager.
     *
     * @param plugin plugin implementing spl
     */
    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
    }

    /**
     * Register multiple new Configs.
     *
     * @param configs Config
     */
    public void registerAll(Config... configs) {
        for (Config c : configs) {
            this.register(c);
        }
    }

    /**
     * Register a new Config.
     *
     * @param config Config
     */
    public void register(Config config) {
        configs.put(config.getName(), config);
    }

    /**
     * Get a config by its name.
     *
     * @param name Config Name
     * @return Config
     */
    public Config get(String name) {
        return configs.get(name);
    }

    /**
     * Initialize all configs.
     */
    public void init() {
        if (!plugin.getDataFolder().exists()) {
            if (!plugin.getDataFolder().mkdir()) throw new ConfigFolderException();
        }
        for (Config c : this.configs.values()) {
            c.init(this.plugin);
        }
    }

    /**
     * Reload all configs.
     */
    public void reload() {
        for (Config c : this.configs.values()) {
            c.reload();
        }
    }

    /**
     * Get all config names.
     *
     * @return config names
     */
    public List<String> getConfigNames() {
        return configs.keySet().stream().toList();
    }
}
