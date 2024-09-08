package dev.masmc05.invapi;

import io.papermc.paper.network.ChannelInitializeListenerHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Plugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        ChannelInitializeListenerHolder.addListener(new NamespacedKey(this, "invapi"), c -> {
            //
            c.pipeline().addBefore("packet_handler", "invapi", new InvApiPacketHandler(c));
        });
    }
}