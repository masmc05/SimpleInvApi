package dev.masmc05.invapi.v1_20_2;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotWrapper extends Slot {
    private final dev.masmc05.invapi.api.slot.Slot apiSlot;

    public SlotWrapper(dev.masmc05.invapi.api.slot.Slot slot) {
        super(((CraftInventory) slot.inventory()).getInventory(), slot.index(), 0, 0);
        this.apiSlot = slot;
    }

    public dev.masmc05.invapi.api.slot.Slot getApiSlot() {
        return this.apiSlot;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.container.getItem(this.getContainerSlot());
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        this.container.setItem(this.getContainerSlot(), stack);
        this.setChanged();
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        return this.container.removeItem(this.getContainerSlot(), amount);
    }

    @Override
    public int getContainerSlot() {
        return this.apiSlot.index();
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.apiSlot.canPlace(CraftItemStack.asBukkitCopy(stack));
    }

    @Override
    public void setChanged() {
        this.apiSlot.changed();
    }

    @Override
    public boolean mayPickup(@NotNull Player playerEntity) {
        return this.apiSlot.canTake();
    }
}
