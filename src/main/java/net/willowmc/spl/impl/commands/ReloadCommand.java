package net.willowmc.spl.impl.commands;

import net.willowmc.spl.SimplePluginLibrary;
import net.willowmc.spl.command.Command;
import net.willowmc.spl.command.CommandContext;
import net.willowmc.spl.command.CommandData;
import net.willowmc.spl.config.Config;
import org.bukkit.plugin.Plugin;

/**
 * Config reload command.
 */
@CommandData(name = "reload", description = "Reload plugin configs.", completion = "config-name")
public class ReloadCommand extends Command {
    private Plugin plugin;

    public ReloadCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return plugin.getName().toLowerCase() + "_" + super.getName();
    }

    @Override
    protected boolean doCommand(CommandContext ctx) {
        SimplePluginLibrary spl = SimplePluginLibrary.getInstance();
        if (spl == null) return false;
        String config = getArg(ctx, 0);
        if (config == null) {
            spl.getCfg().reload();
            ctx.respond("§aReloaded Configs");
        } else {
            Config c = spl.getCfg().get(config);
            if (c == null) {
                ctx.respond("§cConfig Not Found");
            } else {
                c.reload();
                ctx.respond("§aReloaded Config: " + c.getName());
            }
        }
        return true;
    }
}
