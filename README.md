# SimpleInvApi
A very simple inventory api that is based on nms

**I just got really tired of the nonsense of bukkit inventory events and no library offered proper good user experience and flexibility**
> [!NOTE]  
> This was made mostly for internal use, but you can use if you also like this

`build.gradle.kts`
```kt
repositories {
    maven("https://repo.masmc05.dev/repository/maven-snapshots/")
}

dependencies {
    compileOnly("dev.masmc05:SimpleInvApi:0.1-SNAPSHOT")
}
```

Here's an example usage of the API
```java
public class ExampleCreator implements InventoryViewConstructor {
    private final AtomicInteger offset = new AtomicInteger(0);
    //Yes, bukkit inventory (craft inventory class), fully working, except for player.openInventory on non standard sizes
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
        return 18; //Can be any standart chest sizes
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
        //This allows visually changing the slots in player's inventory without actually affecting them
        //Also this needs to be used to listen to clicks
        return new DummySlot(new ItemStack(Material.YELLOW_STAINED_GLASS, index));
    }
    /*
    Use this if you only want to listen to the click
    @Override
    public @NotNull Slot createPlayerInventorySlot(int index) {
        return new SlotAccess(this.player.getInventory(), index, () -> {Action});
    }
    */

    @Override
    public @NotNull Slot createHotbarSlot(int index) {
        //Same as with createPlayerInventorySlot but with hotbar
        return new DummySlot(new ItemStack(Material.BLACK_STAINED_GLASS, index));
    }
}
```

And then open it using
```java
ExampleCreator creator = new ExampleCreator(player);
InventoryApi.createAndOpen(player, creator);
```
