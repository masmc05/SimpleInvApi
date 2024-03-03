package dev.masmc05.invapi.api.slot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a slot in an inventory.
 */
@ApiStatus.OverrideOnly
public interface Slot {
    /**
     * Gets the inventory this slot is in.
     * @return the inventory
     */
    @NotNull Inventory inventory();

    /**
     * Gets the index of this slot in the inventory.
     * @return the index
     */
    int index();

    /**
     * Checks if the specified item can be placed in this slot.
     * @param itemStack the item to check
     * @return true if the item can be placed, false otherwise
     */
    default boolean canPlace(@NotNull ItemStack itemStack) {
        return true;
    }

    /**
     * Checks if the item in this slot can be taken.
     * @return true if the item can be taken, false otherwise
     */
    default boolean canTake() {
        return true;
    }

    /**
     * Notifies the slot that it has changed.
     */
    default void changed() {

    }
}
