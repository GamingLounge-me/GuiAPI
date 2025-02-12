package me.gaminglounge.guiapi;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.gaminglounge.configapi.Language;
import me.gaminglounge.itembuilder.ItemBuilder;
import me.gaminglounge.itembuilder.ItemBuilderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ErrorGUI implements InventoryHolder {
    private static MiniMessage mm;

    public Inventory inv;
    public Inventory returnInv;

    public ErrorGUI(Inventory returnInv, Player p, Component error) {
        mm = MiniMessage.miniMessage();
        this.returnInv = returnInv;
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(
                13,
                new ItemBuilder(Material.BARRIER)
                        .setName(error)
                        .addLoreLine(mm.deserialize(Language.getValue(GuiApi.INSTANCE, p, "error.clickToClose")))
                        .addBothClickEvent("GuiAPI:error_gui_return_to_old_inv")
                        .build());

        inv = new GuiFromMap(this, 3).setItems(items).getInventory();

        ItemBuilderManager.addBothClickEvent("GuiAPI:error_gui_return_to_old_inv", (event) -> {
            event.setCancelled(true);
            if (event.getInventory().getHolder() instanceof ErrorGUI eg) {
                event.getWhoClicked().openInventory(eg.returnInv);
            }
        });

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}
