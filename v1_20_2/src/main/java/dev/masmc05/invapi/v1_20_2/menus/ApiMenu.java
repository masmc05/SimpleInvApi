package dev.masmc05.invapi.v1_20_2.menus;

import dev.masmc05.invapi.v1_20_2.SlotWrapper;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Marker interface for our menu classes.
 */
public interface ApiMenu {
    /**
     * Checks if the specified slot is the top slot of the inventory.
     * @param slot the slot to check
     * @return true if the slot is the top slot, false otherwise
     */
    boolean isTopSlot(int slot);

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
