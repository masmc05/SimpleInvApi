package dev.masmc05.invapi.plugin;

import dev.masmc05.invapi.api.Api;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ServiceLoader;

public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        var api = ServiceLoader.load(Api.class, Plugin.class.getClassLoader())
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(Api::isSupported)
                .findFirst()
                .orElse(null);
        if (api == null) {
            getLogger().severe("The server version is not supported by the Inventory API. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        api.register(this);
    }
}