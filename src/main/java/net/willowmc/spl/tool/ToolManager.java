package net.willowmc.spl.tool;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds tools and information about tools.
 */
public class ToolManager {
    @Getter
    private static final String idKey = "SPL_TOOL_ID";
    private final Map<Integer, Tool> toolMap;

    /**
     * Create a new ToolManager.
     */
    public ToolManager() {
        toolMap = new HashMap<>();
    }

    /**
     * Register a tool.
     *
     * @param tool   tool
     * @param plugin plugin
     */
    public void register(Tool tool, Plugin plugin) {
        toolMap.put(tool.getId(), tool);
        tool.setPlugin(plugin);
    }

    /**
     * Register multiple tools.
     *
     * @param tools  tools
     * @param plugin plugin
     */
    public void registerAll(Tool[] tools, Plugin plugin) {
        for (Tool t : tools) this.register(t, plugin);
    }

    /**
     * Get a tool by its key. Returns null if key is not present.
     *
     * @param key tool key
     * @return tool
     */
    public Tool get(int key) {
        return toolMap.get(key);
    }
}
