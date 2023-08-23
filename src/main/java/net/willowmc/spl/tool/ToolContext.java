package net.willowmc.spl.tool;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Contains Data for use in tool actions
 *
 * @param player player using the tool
 * @param event  event the tool was used in
 * @param tool   tool used
 */
public record ToolContext(Player player, PlayerInteractEvent event, Tool tool) {
}
