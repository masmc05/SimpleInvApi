package dev.masmc05.invapi.v1_20_2;

import dev.masmc05.invapi.api.Api;
import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ApiImpl implements Api, Listener {
    @Override
    public boolean isSupported() {
        try {
            return Delegate.isSupported();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Delegate.join(event);
    }

    @Override
    public @NotNull Inventory createFlexibleInventory(int size, @NotNull Consumer<Player> onOpen, @NotNull Consumer<Player> onClose) {
        return Delegate.createFlexibleInventory(size, onOpen, onClose);
    }

    @Override
    public @NotNull Inventory createSimpleCopy(@NotNull Inventory inventory) {
        return Delegate.createSimpleCopy(inventory);
    }

    @Override
    public void createAndOpen(@NotNull Player player, @NotNull InventoryViewConstructor constructor) {
        Delegate.createAndOpen(player, constructor);
    }
}
