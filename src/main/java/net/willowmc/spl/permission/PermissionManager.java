package net.willowmc.spl.permission;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Handles creating permissions in bukkit.
 */
public class PermissionManager {
    private final PluginManager pm;

    public PermissionManager(JavaPlugin plugin) {
        this.pm = plugin.getServer().getPluginManager();
    }

    /**
     * Add a new permission.
     *
     * @param permission  permission name
     * @param description permission description
     */
    public void register(String permission, String description) {
        this.pm.addPermission(new Permission(permission, description));
    }
}
