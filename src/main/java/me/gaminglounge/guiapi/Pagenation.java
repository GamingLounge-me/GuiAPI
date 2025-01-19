package me.gaminglounge.guiapi;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Pagenation implements InventoryHolder{
    private MiniMessage mm;

    public Inventory inv;
    public Component inventoryName;

    public ItemStack next;
    public ItemStack back;
    public ItemStack close;

    public ItemStack placeholderItem;
    public int[] placeholderItemIndex;

    public List<ItemStack> items;

    public int currentpage;
    public static final int numItemsOnPage =  7 * 4;

    public Pagenation() {
        mm = MiniMessage.miniMessage();
    }

    public Pagenation setPlaceholderItem(ItemStack placeholderItem) {
        this.placeholderItem = placeholderItem;
        return this;
    }

    public Pagenation setplaceholderItemIndex(int[] placeholderItemIndex) {
        this.placeholderItemIndex = placeholderItemIndex;
        return this;
    }

    public Pagenation setInventoryName(Component inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }
    
    public Pagenation setItems(List<ItemStack> items) {
        this.items = items;
        return this;
    }

    public Pagenation setBack(ItemStack back) {
        this.back = back;
        return this;
    }

    public Pagenation setNext(ItemStack next) {
        this.next = next;
        return this;
    }

    public Pagenation setClose(ItemStack close) {
        this.close = close;
        return this;
    }


    @Override
    public @NotNull Inventory getInventory() {

        if (inventoryName == null) {
            this.inventoryName = mm.deserialize("");
        }

        if (placeholderItem == null) {
            int[] tmp = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,46,47,48,50,51,52};
            this.placeholderItemIndex = tmp;
        }

        this.inv = Bukkit.createInventory(this, 9 * 6, inventoryName);

        for (int a:placeholderItemIndex) {
            inv.setItem(a, placeholderItem);
        }

        this.inv.setItem(45, back);

        this.inv.setItem(49, close);

        this.inv.setItem(53, next);

        fillPage(0);

        return this.inv;
    }

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
                    inv.setItem((y + 1) * 9 + x + 1, items.get(y*7 + x +pageNum*numItemsOnPage));
                }
            }
        } catch(IndexOutOfBoundsException | NullPointerException e) {}

        for (int a = inv.firstEmpty(); a<44; a++) {
            if (a == -1) break;
            if (a == 18 || a == 27 || a == 36 ||
            a == 17 || a == 26 || a == 35) continue;
            inv.setItem(a, placeholderItem);
        }

    }

    public void reFillPage(List<ItemStack> newItems) {
        items = newItems;
        fillPage(currentpage);
    }
}
