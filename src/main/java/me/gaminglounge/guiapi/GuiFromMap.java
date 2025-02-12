package me.gaminglounge.guiapi;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.gaminglounge.itembuilder.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class GuiFromMap implements InventoryHolder {
    private final MiniMessage mm = MiniMessage.miniMessage();

    private Inventory inv;
    private final InventoryHolder holder;
    private Component inventoryName;
    private final int row;
    private Map<Integer, ItemStack> items;
    private boolean fill;

    /**
     * 
     * @param holder inventoryholder
     * @param row    how many rows the gui shoud have
     * 
     */
    public GuiFromMap(InventoryHolder holder, int row) {
        this.row = row;
        this.holder = holder;
        this.fill = true;
    }

    /**
     * 
     * @param holder inventoryholder
     * @param row    how many rows the gui shoud have
     * @param fill   if the empty slots should be filles, default = true
     * 
     */
    public GuiFromMap(InventoryHolder holder, int row, boolean fill) {
        this.row = row;
        this.holder = holder;
        this.fill = fill;
    }

    public GuiFromMap setInventoryName(Component inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    public GuiFromMap setItems(Map<Integer, ItemStack> items) {
        this.items = items;
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {

        if (inventoryName == null) {
            this.inventoryName = mm.deserialize("");
        }

        this.inv = Bukkit.createInventory(holder, 9 * row, inventoryName);

        for (int entry : items.keySet()) {
            this.inv.setItem(entry, items.get(entry));
        }

        if (fill) {
            int first = this.inv.firstEmpty();
            if (first == -1)
                return inv;

            for (int i = first; i < inv.getSize(); i++) {
                if (items.containsKey(i))
                    continue;

                this.inv.setItem(i,
                        new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                                .addBothClickEvent("ItemBuilder:cancel")
                                .build());
            }
        }
        return inv;
    }

}
