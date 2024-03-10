package dev.masmc05.invapi.v1_20_2;

import com.google.common.collect.ImmutableSet;
import dev.masmc05.invapi.v1_20_2.menus.ApiMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

public class DelegateContainer<T extends AbstractContainerMenu & ApiMenu> implements Container {
    private final T container;
    private final int size;
    private final ImmutableSet<Container> inventories;
    private final List<SlotWrapper> slots;

    public DelegateContainer(T container) {
        this.container = container;
        this.slots = container.getTopSlots();
        this.size = slots.size();
        var set = ImmutableSet.<Container>builder();
        for (SlotWrapper s : slots) {
            set.add(s.container);
        }
        this.inventories = set.build();
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.inventories.stream().allMatch(Container::isEmpty);
    }

    private Optional<SlotWrapper> getSlot(int slot) {
        return slot < 0 || slot >= this.slots.size() ? Optional.empty() : Optional.of(this.slots.get(slot));
    }
    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.getSlot(slot).map(SlotWrapper::getItem).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return this.getSlot(slot).map(s -> s.remove(amount)).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return this.getSlot(slot).map(s -> s.container.removeItemNoUpdate(s.getContainerSlot()))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.getSlot(slot).ifPresent(s -> s.set(stack));
    }

    @Override
    public int getMaxStackSize() {
        return this.inventories.stream().mapToInt(Container::getMaxStackSize).min().orElse(64);
    }

    @Override
    public void setChanged() {
        this.inventories.forEach(Container::setChanged);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull List<ItemStack> getContents() {
        return new AbstractList<>() {
            @Override
            public ItemStack get(int index) {
                return DelegateContainer.this.getItem(index);
            }

            @Override
            public ItemStack set(int index, ItemStack element) {
                var slotOpt = DelegateContainer.this.getSlot(index);
                if (slotOpt.isEmpty()) return ItemStack.EMPTY;
                var slot = slotOpt.get();
                if (element.isEmpty()) {
                    if (!slot.getApiSlot().canTake()) return ItemStack.EMPTY;
                } else if (!slot.getApiSlot().canPlace(element.asBukkitMirror())) {
                    return ItemStack.EMPTY;
                }
                var old = slot.getItem();
                slot.set(element);
                return old;
            }

            @Override
            public int size() {
                return DelegateContainer.this.size;
            }
        };
    }

    @Override
    public void onOpen(@NotNull CraftHumanEntity who) {
        this.inventories.forEach(c -> c.onOpen(who));
    }

    @Override
    public void onClose(@NotNull CraftHumanEntity who) {
        this.inventories.forEach(c -> c.onClose(who));
    }

    @Override
    public @NotNull List<HumanEntity> getViewers() {
        return this.inventories.stream().findFirst().orElseThrow().getViewers();
    }

    @Override
    public @Nullable InventoryHolder getOwner() {
        return this.container.getOwner();
    }

    @Override
    public void setMaxStackSize(int size) {
        this.inventories.forEach(c -> c.setMaxStackSize(size));
    }

    @SuppressWarnings("NullableProblems") // This should have been nullable by spigot
    @Override
    public @Nullable Location getLocation() {
        return null;
    }

    @Override
    public void clearContent() {
        this.inventories.forEach(Container::clearContent);
    }
}
