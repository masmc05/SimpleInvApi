package dev.masmc05.invapi.api.view;

import dev.masmc05.invapi.api.slot.Slot;
import dev.masmc05.invapi.api.slot.SlotAccess;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Defines data necessary to construct an inventory view.
 */
@ApiStatus.OverrideOnly
public interface InventoryViewConstructor {
    /**
     * Gets the top inventory of this view.
     * @return the top inventory
     */
    @NotNull Inventory getTopInventory();

    /**
     * Gets the player inventory of this view.
     * @return the player inventory
     */
    @NotNull PlayerInventory getPlayerInventory();

    /**
     * Gets the type of this view.
     * @return the type
     */
    default @NotNull InventoryType getType() {
        return this.getTopInventory().getType();
    }

    /**
     * Gets the size of the top inventory.
     * @return the size
     */
    default int getTopSize() {
        return -1;
    }

    /**
     * Creates a slot which maps to a slot in the top inventory.
     * @param index the index
     * @return the slot
     */
    @NotNull Slot createTopSlot(int index);

    /**
     * Creates a slot which maps to a slot in the player inventory.
     * @param index the index
     * @return the slot
     */
    default @NotNull Slot createPlayerInventorySlot(int index) {
        return new SlotAccess(this.getPlayerInventory(), index);
    }

    /**
     * Creates a slot which maps to a slot in the hotbar.
     * @param index the index
     * @return the slot
     */
    default @NotNull Slot createHotbarSlot(int index) {
        return new SlotAccess(this.getPlayerInventory(), index);
    }

    /**
     * Creates a slot which maps to a slot in the additional slots.
     * @param index the index
     * @return the slot
     */
    default @NotNull Slot createAdditionalSlot(int index) {
        throw new UnsupportedOperationException("Additional slots are not supported by this constructor.");
    }

    /**
     * Gets the title of this view.
     * @return the title
     */
    default @NotNull Component title() {
        return Component.empty();
    }
}
