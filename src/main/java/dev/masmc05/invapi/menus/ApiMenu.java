package dev.masmc05.invapi.menus;

import dev.masmc05.invapi.SlotWrapper;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Marker interface for our menu classes.
 */
public interface ApiMenu {

    /**
     * Gets the top slots of the inventory.
     * @return the top slots
     */
    @NotNull List<SlotWrapper> getTopSlots();

    /**
     * Gets the inventory holder of the menu.
     * @return the inventory holder
     */
    @NotNull InventoryHolder getOwner();
}
