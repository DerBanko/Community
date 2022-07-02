package tv.banko.core.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import tv.banko.core.config.GameConfig;
import tv.banko.core.message.TimeMessage;
import tv.banko.core.translation.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Game extends JavaPlugin {

    protected final String name;
    protected final GameTime time;
    protected final NamedTextColor color;
    protected final Translation translation;
    protected final GamePlayers players;
    protected final TimeMessage timeMessage;
    private final GameConfig config;

    protected GameState state;

    public Game(String name, NamedTextColor color) {
        this.name = name;
        this.state = GameState.WAITING;
        this.time = new GameTime(this);
        this.color = color;
        this.translation = new Translation("game", getClassLoader());
        this.players = new GamePlayers();
        this.config = new GameConfig(this);
        this.timeMessage = new TimeMessage(this);
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        load();
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        this.config.load();
        enable();

        this.getServer().getScheduler().runTaskTimer(this, this::task, 20, 20);
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        this.config.save();
        disable();
    }

    public String getGameName() {
        return name;
    }

    public NamedTextColor getColor() {
        return color;
    }

    public GamePlayers getPlayers() {
        return players;
    }

    public Translation getTranslation() {
        return translation;
    }

    public GameTime getTime() {
        return time;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;

        if (this.state != null) {
            return;
        }

        this.getServer().spigot().restart();
        throw new NullPointerException("state is null");
    }

    public void nextState() {
        this.setState(state.next());
    }

    public final Component getPrefix() {
        return Component.text(" ● ", NamedTextColor.DARK_GRAY)
                .append(Component.text(name, color))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY));
    }

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    public void task(BukkitTask task) {
        this.time.task();
    }

    public void load(YamlConfiguration config) {

        if (config.contains("game.time.number")) {
            this.time.setTime(config.getInt("game.time.number"));
        }

        if (config.contains("game.time.type")) {
            this.time.setType(GameTime.Type.valueOf(config.getString("game.time.type")));
        }

        if (config.contains("game.state")) {
            this.state = GameState.valueOf(config.getString("game.state"));
        }

        if (config.contains("game.players")) {
            for (String name : config.getStringList("game.players")) {
                this.players.addPlayer(UUID.fromString(name));
            }
        }

        if (config.contains("game.spectators")) {
            for (String name : config.getStringList("game.spectators")) {
                this.players.addSpectator(UUID.fromString(name));
            }
        }
    }

    public void save(YamlConfiguration config) {
        config.set("game.time.number", this.time.getTime());
        config.set("game.time.type", this.time.getType().name());
        config.set("game.state", this.state.name());

        List<String> players = new ArrayList<>();

        for (UUID uuid : this.players.getPlayers()) {
            players.add(uuid.toString());
        }

        config.set("game.players", players);

        List<String> spectators = new ArrayList<>();

        for (UUID uuid : this.players.getSpectators()) {
            spectators.add(uuid.toString());
        }

        config.set("game.spectators", spectators);
    }

}
