package tv.banko.core.config;

import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.core.Core;
import tv.banko.core.function.Function;

import java.io.File;
import java.io.IOException;

public class FunctionConfig {

    private final Core core;

    private final File file;
    private final YamlConfiguration config;

    public FunctionConfig(Core core) {
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

    public void saveFunction(Function function) {
        function.save(this.config);

        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFunction(Function function) {
        function.load(this.config);
    }
}
