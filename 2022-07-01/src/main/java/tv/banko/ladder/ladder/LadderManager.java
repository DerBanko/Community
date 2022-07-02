package tv.banko.ladderbingo.ladder;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import tv.banko.ladderbingo.LadderBingo;
import tv.banko.ladderbingo.command.LadderCommand;
import tv.banko.ladderbingo.ladder.inventory.LadderInventory;
import tv.banko.ladderbingo.ladder.task.*;
import tv.banko.ladderbingo.listener.BreakBlockListener;
import tv.banko.ladderbingo.listener.BreakItemListener;
import tv.banko.ladderbingo.listener.InventoryListener;
import tv.banko.ladderbingo.listener.KillMobListener;

import java.util.ArrayList;
import java.util.List;

public class LadderManager {

    private final LadderBingo ladder;

    private final LadderInventory inventory;
    private final List<Task> list;

    public LadderManager(LadderBingo ladder) {
        this.ladder = ladder;

        this.inventory = new LadderInventory(this.ladder);
        this.list = new ArrayList<>();

        load();
    }

    public List<BreakBlockTask> getBreakBlockTasks() {
        return this.list.stream().filter(BreakBlockTask.class::isInstance).map(BreakBlockTask.class::cast).toList();
    }

    public List<KillMobTask> getKillMobTasks() {
        return this.list.stream().filter(KillMobTask.class::isInstance).map(KillMobTask.class::cast).toList();
    }

    public List<BreakItemTask> getBreakItemTasks() {
        return this.list.stream().filter(BreakItemTask.class::isInstance).map(BreakItemTask.class::cast).toList();
    }

    public List<AdvancementTask> getAdvancementTasks() {
        return this.list.stream().filter(AdvancementTask.class::isInstance).map(AdvancementTask.class::cast).toList();
    }

    public List<FindItemTask> getFindItemTasks() {
        return this.list.stream().filter(FindItemTask.class::isInstance).map(FindItemTask.class::cast).toList();
    }

    public Task getTask(int id) {
        return this.list.stream().filter(task -> task.id == id).findFirst().orElseThrow(() ->
                new ArrayIndexOutOfBoundsException("the tasks start at 1 and end at 30"));
    }

    public List<Task> getTasks() {
        return list;
    }

    public List<Task> getTaskRange(int taskId) {

        List<Task> list = new ArrayList<>();

        for (int i = taskId; i < (taskId + 10); i++) {
            try {
                list.add(getTask(taskId));
            } catch (Exception e) {
                break;
            }
        }

        return list.stream().sorted().toList();
    }

    public LadderInventory getInventory() {
        return inventory;
    }

    private void load() {
        //<editor-fold desc="Tasks (Hidden)">
        this.list.add(new BreakBlockTask(this.ladder, 1, Material.CRAFTING_TABLE));
        this.list.add(new FindItemTask(this.ladder, 2, Material.DIAMOND_SWORD));
        this.list.add(new AdvancementTask(this.ladder, 3, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/root"))));
        this.list.add(new KillMobTask(this.ladder, 4, EntityType.PIG));
        this.list.add(new FindItemTask(this.ladder, 5, Material.GOLD_BLOCK));
        this.list.add(new BreakBlockTask(this.ladder, 6, Material.SPAWNER));
        this.list.add(new BreakItemTask(this.ladder, 7, Material.GOLDEN_HOE));
        this.list.add(new AdvancementTask(this.ladder, 8, ladder.getServer().getAdvancement(NamespacedKey.minecraft("adventure/avoid_vibration"))));
        this.list.add(new KillMobTask(this.ladder, 9, EntityType.PILLAGER));
        this.list.add(new FindItemTask(this.ladder, 10, Material.NAME_TAG));
        this.list.add(new BreakBlockTask(this.ladder, 11, Material.CHEST));
        this.list.add(new AdvancementTask(this.ladder, 12, ladder.getServer().getAdvancement(NamespacedKey.minecraft("end/root"))));
        this.list.add(new BreakItemTask(this.ladder, 13, Material.STONE_PICKAXE));
        this.list.add(new AdvancementTask(this.ladder, 14, ladder.getServer().getAdvancement(NamespacedKey.minecraft("adventure/trade"))));
        this.list.add(new KillMobTask(this.ladder, 15, EntityType.BLAZE));
        this.list.add(new BreakBlockTask(this.ladder, 16, Material.BELL));
        this.list.add(new FindItemTask(this.ladder, 17, Material.JUKEBOX));
        this.list.add(new AdvancementTask(this.ladder, 18, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/cure_zombie_villager"))));
        this.list.add(new KillMobTask(this.ladder, 19, EntityType.ENDERMAN));
        this.list.add(new BreakItemTask(this.ladder, 20, Material.IRON_AXE));
        this.list.add(new KillMobTask(this.ladder, 21, EntityType.COW));
        this.list.add(new BreakBlockTask(this.ladder, 22, Material.END_STONE));
        this.list.add(new AdvancementTask(this.ladder, 23, ladder.getServer().getAdvancement(NamespacedKey.minecraft("end/find_end_city"))));
        this.list.add(new BreakBlockTask(this.ladder, 24, Material.OAK_LOG));
        this.list.add(new FindItemTask(this.ladder, 25, Material.HOPPER_MINECART));
        this.list.add(new KillMobTask(this.ladder, 26, EntityType.FROG));
        this.list.add(new BreakItemTask(this.ladder, 27, Material.NETHERITE_HOE));
        this.list.add(new AdvancementTask(this.ladder, 28, ladder.getServer().getAdvancement(NamespacedKey.minecraft("adventure/kill_mob_near_sculk_catalyst"))));
        this.list.add(new BreakBlockTask(this.ladder, 29, Material.DIRT));
        this.list.add(new KillMobTask(this.ladder, 30, EntityType.WARDEN));
        //</editor-fold>

        register();
        run();
    }

    private void run() {
        this.ladder.getServer().getScheduler().runTaskTimer(this.ladder, () -> {
            List<AdvancementTask> advancementTasks = getAdvancementTasks();
            List<FindItemTask> findItemTasks = getFindItemTasks();

            this.ladder.getServer().getOnlinePlayers().forEach(player -> {
                advancementTasks.forEach(task -> task.hasReached(player));
                findItemTasks.forEach(task -> task.hasReached(player));
            });
        }, 20, 20);
    }

    private void register() {
        PluginManager plugin = this.ladder.getServer().getPluginManager();
        plugin.registerEvents(new BreakBlockListener(this), this.ladder);
        plugin.registerEvents(new BreakItemListener(this), this.ladder);
        plugin.registerEvents(new KillMobListener(this), this.ladder);
        plugin.registerEvents(new InventoryListener(this), this.ladder);

        this.ladder.getCommand("ladder").setExecutor(new LadderCommand(this.ladder));
    }
}
