package me.gaminglounge.guiapi;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.gaminglounge.configapi.Language;
import me.gaminglounge.itembuilder.ItemBuilder;
import me.gaminglounge.itembuilder.ItemBuilderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Pagenation implements InventoryHolder {
    private final MiniMessage mm;

    private Inventory inv;
    private Inventory returnInventory;
    private Component inventoryName;

    public List<ItemStack> items;

    public Player player;

    public int currentpage;
    public static final int numItemsOnPage = 7 * 4;

    /*
     * Functions needed:
     * - setPlaceholderItem()
     * - setItems()
     */
    public Pagenation(Player p) {
        this.player = p;
        mm = MiniMessage.miniMessage();

        ItemBuilderManager.addBothClickEvent("GuiAPI:nextPage", (e) -> {
            if (e.getInventory().getHolder() instanceof Pagenation pgi) {
                e.setCancelled(true);
                int targetPage = pgi.currentpage + 1;
                if (targetPage < 0 || targetPage > pgi.items.size() / Pagenation.numItemsOnPage) {
                    // error
                    return;
                }
                pgi.fillPage(targetPage);
            }
        });

        ItemBuilderManager.addBothClickEvent("GuiAPI:prevPage", (e) -> {
            if (e.getInventory().getHolder() instanceof Pagenation pgi) {
                e.setCancelled(true);
                int targetPage = pgi.currentpage - 1;
                if (targetPage < 0 || targetPage > (pgi.items.size() + Pagenation.numItemsOnPage - 1)
                        / Pagenation.numItemsOnPage) {
                    // error
                    return;
                }
                pgi.fillPage(targetPage);
            }
        });

        ItemBuilderManager.addBothClickEvent("GuiAPI:pagenation_return_to_old_inv_or_close", (event) -> {
            event.setCancelled(true);
            if (event.getInventory().getHolder() instanceof Pagenation pn) {
                if (pn.returnInventory == null) {
                    event.getWhoClicked().closeInventory();
                } else {
                    event.getWhoClicked().openInventory(pn.returnInventory);
                }
            }
        });

    }

    /**
     * Sets the inventory name
     */
    public Pagenation setInventoryName(Component inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    /**
     * @param items - List of items to be displayed in the pagenation
     */
    public Pagenation setItems(List<ItemStack> items) {
        this.items = items;
        return this;
    }

    /*
     * 
     */
    public Pagenation setBackInv(Inventory returnInventory) {
        this.returnInventory = returnInventory;
        return this;
    }

    /*
     * @return the inventory
     */
    @Override
    public @NotNull Inventory getInventory() {

        if (inventoryName == null) {
            this.inventoryName = mm.deserialize("");
        }

        this.inv = Bukkit.createInventory(this, 9 * 6, inventoryName);

        int[] tmp = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 50, 51, 52 };
        for (int a : tmp) {
            inv.setItem(a,
                    new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                            .setName(inventoryName)
                            .addBothClickEvent("ItemBuilder:cancel")
                            .build());
        }

        this.inv.setItem(45,
                new ItemBuilder(Material.SPECTRAL_ARROW)
                        .setName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, player, "back")))
                        .addBothClickEvent("GuiAPI:prevPage")
                        .build());

        this.inv.setItem(49,
                new ItemBuilder(Material.BARRIER)
                        .setName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, player, "close")))
                        .addBothClickEvent("GuiAPI:pagenation_return_to_old_inv_or_close")
                        .build());

        this.inv.setItem(53,
                new ItemBuilder(Material.ARROW)
                        .setName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, player, "next")))
                        .addBothClickEvent("GuiAPI:nextPage")
                        .build());

        fillPage(0);

        return this.inv;
    }

    /*
     * Changes the displayed items dependend on page
     * 
     * @param pageNum - Page number
     */
    public void fillPage(int pageNum) {
        currentpage = pageNum;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 7; x++) {
                inv.clear((y + 1) * 9 + x + 1);
            }
        }
        try {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 7; x++) {
                    inv.setItem((y + 1) * 9 + x + 1, items.get(y * 7 + x + pageNum * numItemsOnPage));
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
        }

        for (int a = inv.firstEmpty(); a < 44; a++) {
            if (a == -1)
                break;
            if (a == 18 || a == 27 || a == 36 ||
                    a == 17 || a == 26 || a == 35)
                continue;
            inv.setItem(a,
                    new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                            .setName(mm.deserialize(""))
                            .addBothClickEvent("ItemBuilder:cancel")
                            .build());
        }

    }

    /*
     * Refreshed the page in case of item change
     */
    public void refeshPage() {
        fillPage(currentpage);
    }

    /*
     * adds an item to the list of items, refesh with: refeshPage
     */
    public void addItem(ItemStack item) {
        items.add(item);
    }

    /**
     * removed a specific item
     */
    public void removeItem(ItemStack item) {
        items.remove(item);
    }
}
