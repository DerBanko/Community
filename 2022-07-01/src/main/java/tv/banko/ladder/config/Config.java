package tv.banko.ladder.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.ladder.Ladder;
import tv.banko.ladder.ladder.Task;
import tv.banko.ladder.ladder.TaskState;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {

    private final Ladder ladder;

    private final File file;
    private final YamlConfiguration config;

    public Config(Ladder ladder) {
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

    public List<TaskState> getStatesOfTask(Task task) {
        String root = "task." + task.getId();
        ConfigurationSection section = config.getConfigurationSection(root);

        List<TaskState> list = new ArrayList<>();

        if(section == null) {
            return list;
        }

        section.getValues(false).forEach((s, o) -> {
            UUID uuid = UUID.fromString(s.replace(root + ".", ""));
            list.add(new TaskState(uuid, o.toString()));
        });

        return list;
    }
}
