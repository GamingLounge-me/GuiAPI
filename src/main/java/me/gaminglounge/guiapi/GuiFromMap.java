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

public class GuiFromMap implements InventoryHolder{
    private final MiniMessage mm = MiniMessage.miniMessage();

    private Inventory inv;
    private Component inventoryName;
    private final int row;
    private Map<Integer, ItemStack> items;

    public GuiFromMap(int row) {
        this.row = row;
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

        this.inv = Bukkit.createInventory(this, 9 * row, inventoryName);

        for (int entry : items.keySet()) {
            this.inv.setItem(entry, items.get(entry));
        }

        for (int i = 0; i < inv.getSize(); i++) {
            if (!this.inv.getItem(i).isEmpty()) continue;

            this.inv.setItem(i,
                new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .addBothClickEvent("ItemBuilder:cancel")
                .build()
            );
        }

        return inv;
    }

}
