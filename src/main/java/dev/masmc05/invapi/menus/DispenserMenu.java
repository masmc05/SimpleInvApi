package dev.masmc05.invapi.menus;

import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import dev.masmc05.invapi.DelegateContainer;
import dev.masmc05.invapi.SlotWrapper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DispenserMenu extends AbstractContainerMenu implements ApiMenu {
    protected static final int SLOTS_PER_ROW = 9;
    protected final CraftInventoryView<DispenserMenu> bukkitEntity;
    private final InventoryViewConstructor constructor;
    private final List<SlotWrapper> topSlots = new ArrayList<>();
    public DispenserMenu(int syncId, @NotNull InventoryViewConstructor constructor) {
        super(MenuType.GENERIC_3x3, syncId);
        this.constructor = constructor;
        var playerInventory = ((CraftInventoryPlayer) constructor.getPlayerInventory()).getInventory();

        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.addTopSlot(constructor.createTopSlot(i1 + l * 3));
            }
        }

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < SLOTS_PER_ROW; ++i1) {
                this.addSlot(constructor.createPlayerInventorySlot(i1 + l * SLOTS_PER_ROW + SLOTS_PER_ROW));
            }
        }

        for (l = 0; l < SLOTS_PER_ROW; ++l) {
            this.addSlot(constructor.createHotbarSlot(l));
        }
        this.bukkitEntity = new CraftInventoryView<>(playerInventory.player.getBukkitEntity(), new CraftInventory(new DelegateContainer<>(this)), this);
    }


    private void addTopSlot(dev.masmc05.invapi.api.slot.Slot topSlot) {
        var wrap = new SlotWrapper(topSlot);
        this.topSlots.add(wrap);
        this.addSlot(wrap);
    }

    @Override
    public @NotNull List<SlotWrapper> getTopSlots() {
        return this.topSlots;
    }

    @Override
    public @NotNull InventoryHolder getOwner() {
        return this.constructor;
    }

    protected void addSlot(dev.masmc05.invapi.api.slot.Slot slot) {
        this.addSlot(new SlotWrapper(slot));
    }

    @Override
    public @NotNull InventoryView getBukkitView() {
        return this.bukkitEntity;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot1 = this.slots.get(slot);

        if (slot1.hasItem()) {
            ItemStack itemstack1 = slot1.getItem();

            itemstack = itemstack1.copy();
            if (slot < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot1.setByPlayer(ItemStack.EMPTY);
            } else {
                slot1.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot1.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
