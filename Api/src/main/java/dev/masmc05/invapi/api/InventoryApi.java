package dev.masmc05.invapi.api;

import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

/**
 * The main API class for the Inventory API.
 */
public class InventoryApi {
    private InventoryApi() {
        throw new IllegalStateException("Utility class");
    }
    private static final Api api = ServiceLoader.load(Api.class, Api.class.getClassLoader())
            .stream()
            .map(ServiceLoader.Provider::get)
            .filter(Api::isSupported)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No supported inventory API found"));

    /**
     * Creates a new flexible inventory with the specified size.
     * @param size the size of the inventory
     * @return the new inventory
     */
    @Contract(pure = true, value = "_ -> new")
    @NotNull
    public static Inventory createFlexibleInventory(@Positive int size) {
        return api.createFlexibleInventory(size);
    }

    /**
     * Creates a simple copy of the specified inventory.
     * @param inventory the inventory to copy
     * @return the new inventory
     */
    @Contract(pure = true, value = "_ -> new")
    @NotNull
    public static Inventory createSimpleCopy(@NotNull Inventory inventory) {
        return api.createSimpleCopy(inventory);
    }

    /**
     * Creates and opens a new inventory view for the specified player.
     * @param player the player to open the inventory view for
     * @param constructor the inventory view constructor
     */
    public static void createAndOpen(@NotNull Player player, @NotNull InventoryViewConstructor constructor) {
        api.createAndOpen(player, constructor);
    }
}
