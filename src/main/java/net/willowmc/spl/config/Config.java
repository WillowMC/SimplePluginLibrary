package net.willowmc.spl.config;

import lombok.Getter;
import net.willowmc.spl.config.exception.ConfigNotFoundException;
import net.willowmc.spl.config.exception.ConfigReadWriteException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Config Container
 */
public class Config extends YamlConfiguration {
    @Getter
    private final String name;

    private Plugin plugin;

    private List<ConfigCompletion> completions;

    /**
     * Create a new Config.
     *
     * @param name Config Name
     */
    public Config(String name, ConfigCompletion... completions) {
        super();
        this.name = name;
        this.completions = List.of(completions);
    }

    /**
     * Initialize the Config.
     *
     * @param plugin plugin implementing spl
     */
    protected void init(Plugin plugin) {
        this.plugin = plugin;
        String name = this.name.toLowerCase() + ".yml";
        InputStream config = plugin.getResource(name);
        if (config == null) throw new ConfigNotFoundException(this);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(config));
        try {
            File location = new File(plugin.getDataFolder().getPath() + "/" + name);
            if (!location.exists()) {
                if (!location.createNewFile()) throw new ConfigReadWriteException(this);
                yaml.save(location);
            }
            this.load(location);
            for (Map.Entry<String, Object> entry : yaml.getValues(true).entrySet()) {
                this.addDefault(entry.getKey(), entry.getValue());
            }
        } catch (IOException | InvalidConfigurationException e) {
            throw new ConfigReadWriteException(this);
        }
    }

    /**
     * Reloads the Config.
     */
    public void reload() {
        try {
            this.load(plugin.getDataFolder() + "/" + this.name.toLowerCase() + ".yml");
        } catch (IOException | InvalidConfigurationException e) {
            throw new ConfigReadWriteException(this);
        }
    }

    /**
     * Get a string from the config file, including completions and legacy color formatting.
     *
     * @param path YAML Path
     * @return Formatted String
     */
    public String getStringFormatted(String path, String... args) {
        return ChatColor.translateAlternateColorCodes('&', this.complete(this.getString(path, ""), args));
    }

    /**
     * Complete a string with the registered completions.
     *
     * @param toComplete string to complete
     * @return completed string
     */
    private String complete(String toComplete, String... args) {
        int i = 0;
        for (ConfigCompletion c : this.completions) {
            String arg = "";
            if (args.length > i) arg = args[i];
            i++;
            toComplete = toComplete.replaceAll("%" + c.name() + "%", c.function().apply(arg));
        }
        return toComplete;
    }
}
