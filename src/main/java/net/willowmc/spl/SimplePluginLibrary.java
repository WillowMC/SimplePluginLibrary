package net.willowmc.spl;

import lombok.Getter;
import net.willowmc.spl.command.Command;
import net.willowmc.spl.command.CommandManager;
import net.willowmc.spl.command.completion.CompletionRegistry;
import net.willowmc.spl.config.Config;
import net.willowmc.spl.config.ConfigManager;
import net.willowmc.spl.feature.Feature;
import net.willowmc.spl.impl.commands.ReloadCommand;
import net.willowmc.spl.impl.listeners.ToolListener;
import net.willowmc.spl.permission.PermissionManager;
import net.willowmc.spl.tool.ToolManager;
import net.willowmc.spl.util.BitwiseHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SimplePluginLibrary {
    @Getter
    private static SimplePluginLibrary instance = null;
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final PermissionManager perm;
    @Getter
    private final ConfigManager cfg;
    @Getter
    private final CommandManager cmd;
    @Getter
    private final ToolManager tm;

    public SimplePluginLibrary(JavaPlugin plugin, Feature[] features, Config... configs) {
        this.plugin = plugin;
        this.perm = new PermissionManager(plugin);
        this.tm = new ToolManager();
        this.cfg = new ConfigManager(plugin);
        this.cfg.registerAll(configs);
        this.cfg.init();

        List<Command> cmd = new ArrayList<>();

        for (Feature f : features) {
            if (BitwiseHelper.testBit(f.state(), 0)) cmd.add(f.command());
            if (BitwiseHelper.testBit(f.state(), 1)) f.listener().init(plugin);
        }

        new ToolListener(this.tm).init(plugin);

        CompletionRegistry.register("config-name", p -> this.cfg.getConfigNames());
        cmd.add(new ReloadCommand());

        this.cmd = new CommandManager(cmd.toArray(new Command[0]), this);

        instance = this;
    }
}
