package dev.masmc05.invapi.api.slot;

import com.google.common.base.Preconditions;
import dev.masmc05.invapi.api.InventoryApi;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A dummy slot that can be used to create a slot with a custom item.
 * A player can't place or take items from this slot.
 * It is recommended to cache the slot instance if it is used multiple times.
 */
public final class DummySlot implements Slot {
    private final Inventory inventory = InventoryApi.createFlexibleInventory(1);

    /**
     * Creates a new dummy slot with the specified item.
     *
     * @param itemStack the item to set in the slot
     */
    public DummySlot(@NotNull ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack");
        inventory.setItem(0, itemStack);
    }
    @Override
    public @NotNull Inventory inventory() {
        return InventoryApi.createSimpleCopy(this.inventory);
    }

    @Override
    public int index() {
        return 0;
    }

    @Override
    public boolean canPlace(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canTake() {
        return false;
    }
}
