package net.willowmc.spl.tool;

import lombok.Getter;
import lombok.Setter;
import net.willowmc.spl.tool.exception.ToolItemInitException;
import net.willowmc.spl.util.BitwiseHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a Tool Item
 */
public class Tool {
    private final Material mat;
    private final String name;
    private final String lore;
    @Getter
    private final int id;
    private boolean enchanted;
    @Getter
    private boolean consume;
    private byte actions = 0;
    private boolean lActionReg = false;
    private Function<ToolContext, Boolean> lAction;
    private boolean rActionReg = false;
    private Function<ToolContext, Boolean> rAction;
    private final Map<String, ToolData<?>> data;
    @Setter
    private Plugin plugin;

    /**
     * Create a Tool
     *
     * @param mat  Material of the tool
     * @param name Name of the tool
     * @param lore Lore of the tool (seperator: \n)
     */
    public Tool(Material mat, String name, String lore) {
        this.mat = mat;
        this.name = name;
        this.lore = lore;
        this.id = mat.name().hashCode() ^ name.hashCode();
        this.data = new HashMap<>();
    }

    /**
     * Set the tool to be enchanted
     *
     * @return tool instance
     */
    public Tool enchanted() {
        this.enchanted = true;
        return this;
    }

    /**
     * Set the tool to be consumable
     *
     * @return tool instance
     */
    public Tool consume() {
        this.consume = true;
        return this;
    }

    /**
     * Register data stored in the tool
     *
     * @param key  data key
     * @param type data type
     * @param def  default value
     * @param <T>  raw data type
     * @return tool instance
     */
    public <T> Tool data(String key, ToolDataType<T> type, T def) {
        data.put(key, new ToolData<>(key, type, def));
        return this;
    }

    /**
     * Register tool left click function
     *
     * @param laction Left click function
     * @return tool instance
     */
    public Tool registerLeftClick(Function<ToolContext, Boolean> laction) {
        this.lAction = laction;
        if (!lActionReg) actions += 1;
        this.lActionReg = true;
        return this;
    }

    /**
     * Register tool right click function
     *
     * @param raction Right click function
     * @return tool instance
     */
    public Tool registerRightClick(Function<ToolContext, Boolean> raction) {
        this.rAction = raction;
        if (!rActionReg) actions += 2;
        this.rActionReg = true;
        return this;
    }

    /**
     * Check if the tool has a left click function
     *
     * @return has a left click action
     */
    public boolean hasLeft() {
        return BitwiseHelper.testBit(actions, 0);
    }

    /**
     * Check if the tool has a right click function
     *
     * @return has a right click action
     */
    public boolean hasRight() {
        return BitwiseHelper.testBit(actions, 1);
    }

    /**
     * Execute the tools left click action
     *
     * @param e player interact event
     * @return success
     */
    public boolean doLeft(PlayerInteractEvent e) {
        return this.lAction.apply(new ToolContext(e.getPlayer(), e, this));
    }

    /**
     * Execute the tools right click action
     *
     * @param e player interact event
     * @return success
     */
    public boolean doRight(PlayerInteractEvent e) {
        return this.rAction.apply(new ToolContext(e.getPlayer(), e, this));
    }

    /**
     * Get the tool as an item
     *
     * @param toolData tool data for this instance of the tool
     * @return tool as an ItemStack
     */
    public ItemStack getItem(ToolData<?>... toolData) {
        if (this.plugin == null) throw new ToolItemInitException();
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(this.name);
        meta.setLore(Arrays.asList(this.lore.split("\n")));
        if (enchanted) meta.addEnchant(Enchantment.OXYGEN, -1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        PersistentDataContainer pdata = meta.getPersistentDataContainer();
        for (ToolData<?> t : toolData) {
            if (this.data.containsKey(t.key())) {
                pdata.set(new NamespacedKey(this.plugin, t.key().hashCode() + ""), PersistentDataType.STRING, ToolEncryption.encrypt(t.encode()));
            }
        }
        pdata.set(new NamespacedKey(this.plugin, ToolManager.getIdKey()), PersistentDataType.INTEGER, this.id);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Get data for this tool from an ItemStack. Returns null if item or key are invalid.
     * Returns default value if the value was never written.
     *
     * @param key  value key
     * @param type value type
     * @param item ItemStack
     * @param <T>  value type
     * @return value
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, ToolDataType<T> type, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer pdata = meta.getPersistentDataContainer();
        Integer tid = pdata.get(new NamespacedKey(this.plugin, ToolManager.getIdKey()), PersistentDataType.INTEGER);
        if (tid == null || tid != this.id || !this.data.containsKey(key)) return null;
        String data = pdata.get(new NamespacedKey(this.plugin, key.hashCode() + ""), PersistentDataType.STRING);
        if (data == null) return (T) this.data.get(key).data();
        return type.fromData(ToolEncryption.decrypt(data));
    }
}