package dev.masmc05.invapi.api.slot;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Simple slot implementation.
 */
public class SlotAccess implements Slot {
    private final @NotNull Inventory inventory;
    private final int index;
    private final @NotNull Runnable onChange;

    /**
     * Creates a new slot with the specified inventory and index.
     *
     * @param inventory the inventory
     * @param index the index
     */
    public SlotAccess(@NotNull Inventory inventory, int index) {
        Preconditions.checkNotNull(inventory, "inventory");
        this.inventory = inventory;
        this.index = index;
        this.onChange = () -> {};
    }

    /**
     * Creates a new slot with the specified inventory, index and change listener.
     *
     * @param inventory the inventory
     * @param index the index
     * @param onChange the change listener
     */
    public SlotAccess(@NotNull Inventory inventory, int index, @NotNull Runnable onChange) {
        Preconditions.checkNotNull(inventory, "inventory");
        Preconditions.checkNotNull(onChange, "onChange");
        this.inventory = inventory;
        this.index = index;
        this.onChange = onChange;
    }

    @Override
    public @NotNull Inventory inventory() {
        return this.inventory;
    }

    @Override
    public int index() {
        return this.index;
    }

    @Override
    public void changed() {
        this.onChange.run();
    }
}
