package dev.masmc05.invapi.api;

import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the API for the inventory API.
 */
@ApiStatus.Internal
public interface Api {
    /**
     * Checks if the server version is supported by the API.
     *
     * @return true if the server version is supported, false otherwise
     */
    boolean isSupported();

    /**
     * Creates a new flexible inventory with the specified size.
     *
     * @param size the size of the inventory
     * @return the new inventory
     */
    @NotNull Inventory createFlexibleInventory(int size);

    /**
     * Creates a simple copy of the specified inventory.
     *
     * @param inventory the inventory to copy
     * @return the new inventory
     */
    @NotNull Inventory createSimpleCopy(@NotNull Inventory inventory);

    /**
     * Creates and opens a new inventory view for the specified player.
     *
     * @param player the player to open the inventory view for
     * @param constructor the inventory view constructor
     */
    void createAndOpen(@NotNull Player player, @NotNull InventoryViewConstructor constructor);
}
