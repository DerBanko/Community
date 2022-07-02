package tv.banko.ladderbingo.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.ladderbingo.LadderBingo;
import tv.banko.ladderbingo.ladder.Task;
import tv.banko.ladderbingo.ladder.TaskState;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Config {

    private final LadderBingo ladder;

    private final File file;
    private final YamlConfiguration config;

    public Config(LadderBingo ladder) {
        this.ladder = ladder;

        File dir = new File("./plugins/LadderBingo/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(dir, "tasks.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveTasks() {
        ladder.getLadder().getTasks().forEach(task -> {
            String root = "task." + task.getId() + ".";

            task.toMap().forEach((s, o) -> this.config.set(root + s, o));
        });

        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, TaskState> getStatesOfTask(Task task) {
        String root = "task." + task.getId();
        ConfigurationSection section = config.getConfigurationSection(root);

        Map<UUID, TaskState> map = new HashMap<>();

        if(section == null) {
            return map;
        }

        section.getValues(false).forEach((s, o) -> map.put(UUID.fromString(s.replace(root + ".", "")),
                TaskState.valueOf(o.toString())));

        return map;
    }
}
