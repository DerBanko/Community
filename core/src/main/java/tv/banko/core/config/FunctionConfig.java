package tv.banko.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.core.Core;
import tv.banko.core.function.Function;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FunctionsConfig {

    private final Core core;

    private final File file;
    private final YamlConfiguration config;

    public FunctionsConfig(Core core) {
        this.core = core;

        File dir = new File("./plugins/EventCore/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(dir, "functions.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setFunction(Function function) {
        function.save(this.config);

        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStatesOfTask(Task task) {
        String root = "task." + task.getId();
        ConfigurationSection section = config.getConfigurationSection(root);

        Map<UUID, TaskState> map = new HashMap<>();

        if (section == null) {
            return map;
        }

        section.getValues(false).forEach((s, o) -> map.put(UUID.fromString(s.replace(root + ".", "")),
                TaskState.valueOf(o.toString())));

        return map;
    }
}
