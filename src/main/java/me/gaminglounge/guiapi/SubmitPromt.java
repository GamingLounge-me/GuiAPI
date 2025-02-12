package me.gaminglounge.guiapi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.gaminglounge.configapi.Language;
import me.gaminglounge.itembuilder.ItemBuilder;
import me.gaminglounge.itembuilder.ItemBuilderManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SubmitPromt implements InventoryHolder {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public Inventory inv, returnInv;
    private Consumer<InventoryClickEvent> event;
    private boolean close;

    /**
     * 
     * @param returnInv the inv that is opened after submit/caalcel
     * @param p         the player (for the language)
     * @param event     The event that will be executed on submit
     * 
     */
    public SubmitPromt(Inventory returnInv, Player p, Consumer<InventoryClickEvent> event) {
        this(returnInv, p, event, false);
    }

    /**
     * 
     * @param returnInv the inv that is opened after submit/caalcel
     * @param p         the player (for the language)
     * @param event     The event that will be executed on submit
     * @param close     default = false, if true it will close the inventory on
     *                  submit
     * 
     */
    public SubmitPromt(Inventory returnInv, Player p, Consumer<InventoryClickEvent> acceptEvent, boolean close) {
        this.returnInv = returnInv;
        this.event = acceptEvent;
        this.close = close;
        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(
                2,
                new ItemBuilder(Material.GREEN_WOOL)
                        .setName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, p, "submit.submit")))
                        .addBothClickEvent("GuiAPI:submitpromt_submit")
                        .build());

        items.put(
                6,
                new ItemBuilder(Material.RED_WOOL)
                        .setName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, p, "submit.cancel")))
                        .addBothClickEvent("GuiAPI:submitpromt_cancel")
                        .build());

        inv = new GuiFromMap(this, 1).setItems(items)
                .setInventoryName(mm.deserialize(Language.getValue(GuiApi.INSTANCE, p, "submit.name"))).getInventory();

        ItemBuilderManager.addBothClickEvent("GuiAPI:submitpromt_cancel", (event) -> {
            event.setCancelled(true);
            if (event.getInventory().getHolder() instanceof SubmitPromt sp) {
                event.getWhoClicked().openInventory(sp.returnInv);
            }
        });

        ItemBuilderManager.addBothClickEvent("GuiAPI:submitpromt_submit", (event) -> {
            event.setCancelled(true);
            if (event.getInventory().getHolder() instanceof SubmitPromt sp) {
                sp.event.accept(event);
                if (close)
                    event.getWhoClicked().closeInventory();
                else
                    event.getWhoClicked().openInventory(sp.returnInv);
            }
        });

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

}
