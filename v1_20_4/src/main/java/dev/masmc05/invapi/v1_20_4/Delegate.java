package dev.masmc05.invapi.v1_20_4;

import com.google.common.base.Preconditions;
import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import dev.masmc05.invapi.v1_20_4.menus.ChestMenu;
import dev.masmc05.invapi.v1_20_4.menus.DispenserMenu;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Delegate {
    public static boolean isSupported() {
        try {
            return SharedConstants.getCurrentVersion().getId().equals("1.20.4");
        } catch (Throwable t) {
            return false;
        }
    }

    public static void join(PlayerJoinEvent event) {
        var player = (CraftPlayer) event.getPlayer();
        player.getHandle().connection.connection.channel.pipeline().addBefore("packet_handler", "invapi", new InvApiPacketHandler(player.getHandle()));
    }
    public static @NotNull Inventory createFlexibleInventory(int size, @NotNull Consumer<Player> onOpen, @NotNull Consumer<Player> onClose) {
        return new CraftInventory(new SimpleListenerContainer(size, onOpen, onClose));
    }
    public static @NotNull Inventory createSimpleCopy(@NotNull Inventory inventory) {
        if (inventory instanceof CraftInventory craftInventory) {
            var handle = craftInventory.getInventory();
            if (handle instanceof SimpleContainer simpleContainer) {
                var arr = simpleContainer.getContents()
                        .stream()
                        .map(ItemStack::copy)
                        .toArray(ItemStack[]::new);
                return new CraftInventory(new SimpleContainer(arr));
            }
            var inv = new SimpleContainer(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                inv.setItem(i, handle.getItem(i).copy());
            }
            return new CraftInventory(inv);
        } else {
            var inv = new CraftInventory(new SimpleContainer(inventory.getSize()));
            for (int i = 0; i < inventory.getSize(); i++) {
                inv.setItem(i, inventory.getItem(i));
            }
            return inv;
        }
    }
    public static void createAndOpen(@NotNull Player player, @NotNull InventoryViewConstructor constructor) {
        Preconditions.checkArgument(constructor.getType() == InventoryType.CHEST || constructor.getType() == InventoryType.DISPENSER, "Only chest and dispenser inventories are supported at the moment");
        ServerPlayer player1 = ((CraftPlayer) player).getHandle();
        MenuProvider provider = switch (constructor.getType()) {
            case CHEST -> new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return PaperAdventure.asVanilla(constructor.title());
                }
                @Override
                public @NotNull AbstractContainerMenu createMenu(int syncId,
                                                                 net.minecraft.world.entity.player.@NotNull Inventory playerInventory,
                                                                 net.minecraft.world.entity.player.@NotNull Player player) {
                    var size = constructor.getTopSize();
                    MenuType<?> type;
                    int rows;
                    switch (size) {
                        case 9 -> {
                            type = MenuType.GENERIC_9x1;
                            rows = 1;
                        }
                        case 18 -> {
                            type = MenuType.GENERIC_9x2;
                            rows = 2;
                        }
                        case 27 -> {
                            type = MenuType.GENERIC_9x3;
                            rows = 3;
                        }
                        case 36 -> {
                            type = MenuType.GENERIC_9x4;
                            rows = 4;
                        }
                        case 45 -> {
                            type = MenuType.GENERIC_9x5;
                            rows = 5;
                        }
                        case 54 -> {
                            type = MenuType.GENERIC_9x6;
                            rows = 6;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + size);
                    }
                    return new ChestMenu(type, rows, syncId, constructor);
                }
            };
            case DISPENSER -> new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return PaperAdventure.asVanilla(constructor.title());
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(int syncId,
                                                                 net.minecraft.world.entity.player.@NotNull Inventory playerInventory,
                                                                 net.minecraft.world.entity.player.@NotNull Player player) {
                    return new DispenserMenu(syncId, constructor);
                }
            };
            default -> throw new IllegalStateException("Unexpected value: " + constructor.getType());
        };
        player1.openMenu(provider);
    }
}
