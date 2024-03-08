package dev.masmc05.invapi.v1_20_4.menus;

import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import dev.masmc05.invapi.v1_20_4.SlotWrapper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class ChestMenu extends AbstractContainerMenu implements ApiMenu {
    protected static final int SLOTS_PER_ROW = 9;

    protected final Container container;
    protected final int containerRows;
    protected final CraftInventoryView bukkitEntity;
    public ChestMenu(@NotNull MenuType<?> type,
                     int rows,
                     int syncId,
                     @NotNull InventoryViewConstructor constructor) {
        super(type, syncId);
        this.container = ((CraftInventory) constructor.getTopInventory()).getInventory();
        this.containerRows = rows;
        var playerInventory = ((CraftInventoryPlayer) constructor.getPlayerInventory()).getInventory();

        this.bukkitEntity = new CraftInventoryView(playerInventory.player.getBukkitEntity(), new CraftInventory(this.container), this);

        int l;
        int i1;

        for (l = 0; l < this.containerRows; ++l) {
            for (i1 = 0; i1 < SLOTS_PER_ROW; ++i1) {
                this.addSlot(constructor.createTopSlot(i1 + l * SLOTS_PER_ROW));
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
        Slot slot1 = this.slots.get(slot);

        if (!slot1.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack1 = slot1.getItem();
        ItemStack itemstack = itemstack1.copy();
        if (slot < this.containerRows * SLOTS_PER_ROW) {
            if (!this.moveItemStackTo(itemstack1, this.containerRows * SLOTS_PER_ROW, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * SLOTS_PER_ROW, false)) {
            return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
            slot1.setByPlayer(ItemStack.EMPTY);
        } else {
            slot1.setChanged();
        }

        return itemstack;
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
