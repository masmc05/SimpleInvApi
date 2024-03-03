package dev.masmc05.invapi.api.view;

import com.google.common.base.Preconditions;
import dev.masmc05.invapi.api.slot.ImmutableSlot;
import dev.masmc05.invapi.api.slot.Slot;
import dev.masmc05.invapi.api.slot.SlotAccess;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

/**
 * A simple inventory view constructor which only allows viewing the inventory.
 */
public class ViewOnlyInventoryConstructor implements InventoryViewConstructor {
    private final Inventory topInventory;
    private final PlayerInventory playerInventory;
    private final IntConsumer onChange;
    private final int offset;
    private final int size;
    private final Component title;

    /**
     * Creates a new view only inventory constructor with the specified title, top inventory and player inventory.
     * @param title the title
     * @param topInventory the top inventory
     * @param playerInventory the player inventory
     */
    public ViewOnlyInventoryConstructor(Component title, Inventory topInventory, PlayerInventory playerInventory) {
        this(title, topInventory, playerInventory, 0, topInventory.getSize());
    }

    /**
     * Creates a new view only inventory constructor with the specified title, top inventory, player inventory and offset.
     * @param title the title
     * @param topInventory the top inventory
     * @param playerInventory the player inventory
     * @param offset the offset
     * @param size the size
     */
    public ViewOnlyInventoryConstructor(Component title, Inventory topInventory, PlayerInventory playerInventory, int offset, int size) {
        this(title, topInventory, playerInventory, offset, size, i -> {});
    }

    /**
     * Creates a new view only inventory constructor with the specified title, top inventory, player inventory and change listener.
     * @param title the title
     * @param topInventory the top inventory
     * @param playerInventory the player inventory
     * @param onChange the change listener
     */
    public ViewOnlyInventoryConstructor(Component title, Inventory topInventory, PlayerInventory playerInventory, IntConsumer onChange) {
        this(title, topInventory, playerInventory, 0, topInventory.getSize(), onChange);
    }

    /**
     * Creates a new view only inventory constructor with the specified title, top inventory, player inventory, offset and change listener.
     * @param title the title
     * @param topInventory the top inventory
     * @param playerInventory the player inventory
     * @param offset the offset
     * @param size the size
     * @param onChange the change listener
     */
    public ViewOnlyInventoryConstructor(Component title, Inventory topInventory, PlayerInventory playerInventory, int offset, int size, IntConsumer onChange) {
        Preconditions.checkNotNull(title, "title");
        Preconditions.checkNotNull(topInventory, "topInventory");
        Preconditions.checkNotNull(playerInventory, "playerInventory");
        Preconditions.checkNotNull(onChange, "onChange");
        Preconditions.checkArgument(offset >= 0, "offset must be greater or equal to 0");
        this.topInventory = topInventory;
        this.playerInventory = playerInventory;
        this.onChange = onChange;
        this.offset = offset;
        this.size = size;
        this.title = title;
    }

    @Override
    public final @NotNull InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getTopSize() {
        return this.size;
    }

    @Override
    public @NotNull Inventory getTopInventory() {
        return this.topInventory;
    }

    @Override
    public @NotNull PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    @Override
    public @NotNull Slot createTopSlot(int index) {
        int idx = index + offset;
        return new ImmutableSlot(this.topInventory, idx, () -> onChange.accept(idx));
    }

    @Override
    public @NotNull Slot createPlayerInventorySlot(int index) {
        return new SlotAccess(this.playerInventory, index);
    }

    @Override
    public @NotNull Slot createHotbarSlot(int index) {
        return new SlotAccess(this.playerInventory, index);
    }

    @Override
    public @NotNull Component title() {
        return this.title;
    }
}
