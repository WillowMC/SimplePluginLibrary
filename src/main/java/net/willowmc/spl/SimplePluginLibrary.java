package net.willowmc.spl;

import lombok.Getter;
import net.willowmc.spl.command.Command;
import net.willowmc.spl.command.CommandManager;
import net.willowmc.spl.permission.PermissionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePluginLibrary {
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final PermissionManager perm;
    @Getter
    private final CommandManager cmd;

    public SimplePluginLibrary(JavaPlugin plugin, Command[] commands) {
        this.plugin = plugin;
        this.perm = new PermissionManager(plugin);
        this.cmd = new CommandManager(commands, this);
    }
}
