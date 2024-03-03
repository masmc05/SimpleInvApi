package dev.masmc05.invapi.plugin;

import dev.masmc05.invapi.api.InventoryApi;
import dev.masmc05.invapi.api.slot.DummySlot;
import dev.masmc05.invapi.api.slot.Slot;
import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class ExampleCreator implements InventoryViewConstructor {
    private final AtomicInteger offset = new AtomicInteger(0);
    private final Inventory topInventory = InventoryApi.createFlexibleInventory(120);
    private final Player player;

    public ExampleCreator(Player player) {
        this.player = player;
        for (int i = 1; i <= 64; i++) {
            topInventory.setItem(i - 1, new ItemStack(Material.DIAMOND, i));
        }
        for (int i = 64; i < 120; i++) {
            topInventory.setItem(i, new ItemStack(Material.EMERALD, i - 63));
        }
    }
    @Override
    public @NotNull Inventory getTopInventory() {
        return this.topInventory;
    }

    @Override
    public @NotNull PlayerInventory getPlayerInventory() {
        return this.player.getInventory();
    }

    @Override
    public int getTopSize() {
        return 18;
    }

    @Override
    public @NotNull Slot createTopSlot(int index) {
        return new Slot() {
            @Override
            public @NotNull Inventory inventory() {
                return ExampleCreator.this.topInventory;
            }

            @Override
            public int index() {
                return ExampleCreator.this.offset.get() + index;
            }

            @Override
            public boolean canPlace(@NotNull ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean canTake() {
                return false;
            }

            @Override
            public void changed() {
                offset.incrementAndGet();
                player.updateInventory();
            }
        };
    }

    @Override
    public @NotNull Slot createPlayerInventorySlot(int index) {
        return new DummySlot(new ItemStack(Material.YELLOW_STAINED_GLASS, index));
    }

    @Override
    public @NotNull Slot createHotbarSlot(int index) {
        return new DummySlot(new ItemStack(Material.BLACK_STAINED_GLASS, index));
    }
}