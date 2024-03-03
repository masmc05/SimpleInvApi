package dev.masmc05.invapi.plugin;

import dev.masmc05.invapi.api.Api;
import dev.masmc05.invapi.api.InventoryApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ServiceLoader;

public class Plugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        if (ServiceLoader.load(Api.class, Plugin.class.getClassLoader())
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(Api::isSupported)
                .findFirst()
                .isEmpty()) {
            getLogger().severe("The server version is not supported by the Inventory API. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Create and open a new inventory view for the player 1 tick after joining
        getServer().getScheduler().runTask(this, () -> {
            ExampleCreator creator = new ExampleCreator(event.getPlayer());
            InventoryApi.createAndOpen(event.getPlayer(), creator);
        });
    }
}