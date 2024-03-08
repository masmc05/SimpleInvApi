package dev.masmc05.invapi.v1_20_4;

import net.minecraft.world.SimpleContainer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class SimpleListenerContainer extends SimpleContainer {
    private final Consumer<Player> onOpen;
    private final Consumer<Player> onClose;
    public SimpleListenerContainer(int size, Consumer<Player> onOpen, Consumer<Player> onClose) {
        super(size);
        this.onOpen = onOpen;
        this.onClose = onClose;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        super.onOpen(who);
        if (who instanceof Player player) {
            onOpen.accept(player);
        }
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        super.onClose(who);
        if (who instanceof Player player) {
            onClose.accept(player);
        }
    }
}
