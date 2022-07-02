package tv.banko.ladder.ladder;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;
import tv.banko.ladder.Ladder;
import tv.banko.ladder.command.LadderCommand;
import tv.banko.ladder.ladder.inventory.LadderInventory;
import tv.banko.ladder.ladder.task.*;
import tv.banko.ladder.listener.BreakBlockListener;
import tv.banko.ladder.listener.BreakItemListener;
import tv.banko.ladder.listener.InventoryListener;
import tv.banko.ladder.listener.KillMobListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class LadderManager {

    private final Ladder ladder;

    private final LadderInventory inventory;
    private final List<Task> list;

    public LadderManager(Ladder ladder) {
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

    @Nullable
    public Task getCurrentTask(Player player) {
        return this.list.stream().filter(task -> task.getState(player).getType().equals(TaskState.Type.TASK))
                .findFirst().orElse(null);
    }

    public List<Task> getTasks() {
        return list;
    }

    public List<TaskState> getReversedTaskStates() {
        List<TaskState> list = new ArrayList<>();
        List<UUID> uuids = new ArrayList<>();

        for (Task task : this.list.stream().sorted((o1, o2) -> Integer.compare(0, o1.compareTo(o2))).toList()) {
            List<TaskState> reached = task.getReached();

            if (reached.isEmpty()) {
                continue;
            }

            reached.stream().filter(state -> !uuids.contains(state.getUUID()))
                    .sorted(Comparator.comparingLong(TaskState::getReached)).forEach(taskState -> {
                list.add(taskState);
                uuids.add(taskState.getUUID());
            });
        }

        return list;
    }
    public List<Task> getTaskRange(int taskId) {

        List<Task> list = new ArrayList<>();

        for (int i = taskId; i < (taskId + 7); i++) {
            try {
                list.add(getTask(i));
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
        this.list.add(new FindItemTask(this.ladder, 2, Material.IRON_SWORD));
        this.list.add(new AdvancementTask(this.ladder, 3, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/root"))));
        this.list.add(new KillMobTask(this.ladder, 4, EntityType.PIG));
        this.list.add(new FindItemTask(this.ladder, 5, Material.GOLD_BLOCK));
        this.list.add(new BreakBlockTask(this.ladder, 6, Material.SPAWNER));
        this.list.add(new BreakItemTask(this.ladder, 7, Material.GOLDEN_HOE));
        this.list.add(new AdvancementTask(this.ladder, 8, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/lava_bucket"))));
        this.list.add(new KillMobTask(this.ladder, 9, EntityType.PILLAGER));
        this.list.add(new FindItemTask(this.ladder, 10, Material.DIAMOND));
        this.list.add(new BreakBlockTask(this.ladder, 11, Material.CHEST));
        this.list.add(new AdvancementTask(this.ladder, 12, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/enchant_item"))));
        this.list.add(new BreakItemTask(this.ladder, 13, Material.STONE_PICKAXE));
        this.list.add(new AdvancementTask(this.ladder, 14, ladder.getServer().getAdvancement(NamespacedKey.minecraft("adventure/trade"))));
        this.list.add(new KillMobTask(this.ladder, 15, EntityType.BLAZE));
        this.list.add(new BreakBlockTask(this.ladder, 16, Material.BELL));
        this.list.add(new FindItemTask(this.ladder, 17, Material.JUKEBOX));
        this.list.add(new AdvancementTask(this.ladder, 18, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/shiny_gear"))));
        this.list.add(new KillMobTask(this.ladder, 19, EntityType.ENDERMAN));
        this.list.add(new BreakItemTask(this.ladder, 20, Material.IRON_AXE));
        this.list.add(new KillMobTask(this.ladder, 21, EntityType.COW));
        this.list.add(new BreakBlockTask(this.ladder, 22, Material.CRACKED_STONE_BRICKS));
        this.list.add(new AdvancementTask(this.ladder, 23, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/obtain_ancient_debris"))));
        this.list.add(new BreakBlockTask(this.ladder, 24, Material.OAK_LOG));
        this.list.add(new FindItemTask(this.ladder, 25, Material.HOPPER_MINECART));
        this.list.add(new AdvancementTask(this.ladder, 26, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/explore_nether"))));
        this.list.add(new FindItemTask(this.ladder, 27, Material.NETHERITE_HOE));
        this.list.add(new AdvancementTask(this.ladder, 28, ladder.getServer().getAdvancement(NamespacedKey.minecraft("adventure/voluntary_exile"))));
        this.list.add(new FindItemTask(this.ladder, 29, Material.WITHER_SKELETON_SKULL));
        this.list.add(new BreakBlockTask(this.ladder, 30, Material.DIRT));
        //</editor-fold>

        /*//<editor-fold desc="Test Tasks">
        this.list.add(new BreakBlockTask(this.ladder, 1, Material.OAK_LOG));
        this.list.add(new FindItemTask(this.ladder, 2, Material.WOODEN_SWORD));
        this.list.add(new AdvancementTask(this.ladder, 3, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/upgrade_tools"))));
        this.list.add(new KillMobTask(this.ladder, 4, EntityType.COD));
        this.list.add(new FindItemTask(this.ladder, 5, Material.HOPPER));
        this.list.add(new BreakBlockTask(this.ladder, 6, Material.GRASS_BLOCK));
        this.list.add(new BreakItemTask(this.ladder, 7, Material.WOODEN_HOE));
        this.list.add(new AdvancementTask(this.ladder, 8, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/iron_tools"))));
        this.list.add(new KillMobTask(this.ladder, 9, EntityType.CHICKEN));
        this.list.add(new FindItemTask(this.ladder, 10, Material.IRON_INGOT));
        this.list.add(new BreakBlockTask(this.ladder, 11, Material.COBBLESTONE));
        this.list.add(new AdvancementTask(this.ladder, 12, ladder.getServer().getAdvancement(NamespacedKey.minecraft("story/smelt_iron"))));
        this.list.add(new BreakItemTask(this.ladder, 13, Material.WOODEN_PICKAXE));
        this.list.add(new AdvancementTask(this.ladder, 14, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/find_bastion"))));
        this.list.add(new KillMobTask(this.ladder, 15, EntityType.PIGLIN));
        this.list.add(new BreakBlockTask(this.ladder, 16, Material.NETHERRACK));
        this.list.add(new FindItemTask(this.ladder, 17, Material.GOLD_NUGGET));
        this.list.add(new AdvancementTask(this.ladder, 18, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/distract_piglin"))));
        this.list.add(new KillMobTask(this.ladder, 19, EntityType.WITHER_SKELETON));
        this.list.add(new BreakItemTask(this.ladder, 20, Material.GOLDEN_PICKAXE));
        this.list.add(new KillMobTask(this.ladder, 21, EntityType.ZOMBIFIED_PIGLIN));
        this.list.add(new BreakBlockTask(this.ladder, 22, Material.NETHER_BRICKS));
        this.list.add(new AdvancementTask(this.ladder, 23, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/loot_bastion"))));
        this.list.add(new BreakBlockTask(this.ladder, 24, Material.CHEST));
        this.list.add(new FindItemTask(this.ladder, 25, Material.ANCIENT_DEBRIS));
        this.list.add(new AdvancementTask(this.ladder, 26, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/explore_nether"))));
        this.list.add(new FindItemTask(this.ladder, 27, Material.NETHERITE_INGOT));
        this.list.add(new AdvancementTask(this.ladder, 28, ladder.getServer().getAdvancement(NamespacedKey.minecraft("nether/obtain_blaze_rod"))));
        this.list.add(new FindItemTask(this.ladder, 29, Material.DIAMOND_HORSE_ARMOR));
        this.list.add(new BreakBlockTask(this.ladder, 30, Material.SPAWNER));
        //</editor-fold>*/

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
