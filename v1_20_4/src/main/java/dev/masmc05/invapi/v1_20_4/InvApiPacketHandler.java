package dev.masmc05.invapi.v1_20_4;

import dev.masmc05.invapi.v1_20_4.menus.ApiMenu;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class InvApiPacketHandler extends ChannelDuplexHandler {
    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ApiImpl.class);
    private final ServerPlayer handle;

    public InvApiPacketHandler(ServerPlayer handle) {
        this.handle = handle;
    }

    //Listener for click packet
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ServerboundContainerClickPacket packet) || !(this.handle.containerMenu instanceof ApiMenu)) {
            ctx.fireChannelRead(msg);
            return;
        }
        handle.getBukkitEntity().getScheduler().run(plugin, t -> {
            if (!(this.handle.containerMenu instanceof ApiMenu)) {
                ctx.fireChannelRead(msg);
                return;
            }
            this.handle.resetLastActionTime();
            if (this.handle.containerMenu.containerId != packet.getContainerId()) {
                return;
            }
            if (this.handle.isSpectator()) {
                this.handle.containerMenu.sendAllDataToRemote();
                return;
            } else if (!this.handle.containerMenu.stillValid(this.handle)) {
                plugin.getComponentLogger().debug("Player {} interacted with invalid menu {}", this.handle, this.handle.containerMenu);
                return;
            }
            int slotNum = packet.getSlotNum();
            if (!this.handle.containerMenu.isValidSlotIndex(slotNum)) {
                plugin.getComponentLogger().debug(
                        "Player {} clicked invalid slot index: {}, available slots: {}", this.handle.getName(), slotNum, this.handle.containerMenu.slots.size()
                );
                return;
            }
            boolean flag = packet.getStateId() != this.handle.containerMenu.getStateId();
            this.handle.containerMenu.suppressRemoteUpdates();
            this.handle.containerMenu.clicked(slotNum, packet.getButtonNum(), packet.getClickType(), this.handle);

            for (var entry : Int2ObjectMaps.fastIterable(packet.getChangedSlots())) {
                this.handle.containerMenu.setRemoteSlotNoCopy(entry.getIntKey(), entry.getValue());
            }

            this.handle.containerMenu.setRemoteCarried(packet.getCarriedItem());
            this.handle.containerMenu.resumeRemoteUpdates();
            if (flag) {
                this.handle.containerMenu.broadcastFullState();
            } else {
                this.handle.containerMenu.broadcastChanges();
            }
        }, null);
    }

}
