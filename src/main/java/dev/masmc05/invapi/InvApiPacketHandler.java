package dev.masmc05.invapi;

import com.google.common.base.Suppliers;
import dev.masmc05.invapi.menus.ApiMenu;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public class InvApiPacketHandler extends ChannelDuplexHandler {
    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ApiImpl.class);
    private final Supplier<ServerPlayer> handle;

    public InvApiPacketHandler(Channel channel) {
        this.handle = Suppliers.memoize(() -> channel.pipeline().get(Connection.class).getPlayer());
    }

    //Listener for click packet
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ServerboundContainerClickPacket packet) || !(this.handle.get() instanceof ApiMenu)) {
            ctx.fireChannelRead(msg);
            return;
        }
        ServerPlayer handle = this.handle.get();
        handle.getBukkitEntity().getScheduler().run(plugin, t -> {
            if (!(handle.containerMenu instanceof ApiMenu)) {
                ctx.fireChannelRead(msg);
                return;
            }
            handle.resetLastActionTime();
            if (handle.containerMenu.containerId != packet.getContainerId()) {
                return;
            }
            if (handle.isSpectator()) {
                handle.containerMenu.sendAllDataToRemote();
                return;
            } else if (!handle.containerMenu.stillValid(handle)) {
                plugin.getComponentLogger().debug("Player {} interacted with invalid menu {}", handle, handle.containerMenu);
                return;
            }
            int slotNum = packet.getSlotNum();
            if (!handle.containerMenu.isValidSlotIndex(slotNum)) {
                plugin.getComponentLogger().debug(
                        "Player {} clicked invalid slot index: {}, available slots: {}", handle.getName(), slotNum, handle.containerMenu.slots.size()
                );
                return;
            }
            boolean flag = packet.getStateId() != handle.containerMenu.getStateId();
            handle.containerMenu.suppressRemoteUpdates();
            handle.containerMenu.clicked(slotNum, packet.getButtonNum(), packet.getClickType(), handle);

            for (var entry : Int2ObjectMaps.fastIterable(packet.getChangedSlots())) {
                handle.containerMenu.setRemoteSlotNoCopy(entry.getIntKey(), entry.getValue());
            }

            handle.containerMenu.setRemoteCarried(packet.getCarriedItem());
            handle.containerMenu.resumeRemoteUpdates();
            if (flag) {
                handle.containerMenu.broadcastFullState();
            } else {
                handle.containerMenu.broadcastChanges();
            }
        }, null);
    }

}
