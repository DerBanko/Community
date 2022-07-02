package tv.banko.core.config;

import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.core.Core;
import tv.banko.core.game.Game;

import java.io.File;
import java.io.IOException;

public class GameConfig {

    private final Game game;

    private final File file;
    private final YamlConfiguration config;

    public GameConfig(Game game) {
        this.game = game;

        File dir = new File("./plugins/EventCore/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(dir, game.getGameName().toLowerCase() + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        this.game.load(this.config);
    }

    public void save() {
        this.game.save(this.config);

        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
