package net.willowmc.spl.impl.listeners;

import net.willowmc.spl.event.Listener;
import net.willowmc.spl.tool.Tool;
import net.willowmc.spl.tool.ToolManager;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Tool Listener
 */
public class ToolListener extends Listener {
    private final ToolManager toolManager;

    public ToolListener(ToolManager toolManager) {
        this.toolManager = toolManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        Integer key = meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, ToolManager.getIdKey()), PersistentDataType.INTEGER);
        if (key == null) return;
        Tool tool = toolManager.get(key);
        if (tool == null) return;
        boolean success = false;
        if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && tool.hasLeft()) {
            success = tool.doLeft(event);
        } else if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && tool.hasRight()) {
            success = tool.doRight(event);
        }
        if (tool.isConsume() && success) {
            EquipmentSlot slot = event.getHand();
            if (slot == null) return;
            ItemStack invItem = event.getPlayer().getInventory().getItem(slot);
            if (invItem == null) return;
            invItem.setAmount(invItem.getAmount() - 1);
        }
        event.setCancelled(true);
    }
}
