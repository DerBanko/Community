package tv.banko.core;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tv.banko.core.command.MaxPlayersCommand;
import tv.banko.core.command.SpectateCommand;
import tv.banko.core.command.StartCommand;
import tv.banko.core.function.FunctionManager;
import tv.banko.core.game.Game;
import tv.banko.core.listener.player.*;
import tv.banko.core.listener.server.PingListener;
import tv.banko.core.listener.server.PluginListener;
import tv.banko.core.translation.CoreTranslation;

public class Core extends JavaPlugin {

    private FunctionManager function;
    private CoreTranslation translation;
    private Game game;

    @Override
    public void onEnable() {
        this.translation = new CoreTranslation();
        this.function = new FunctionManager(this);

        this.function.enable();
        this.loadCommands();
        this.loadListeners();
    }

    @Override
    public void onDisable() {
        this.function.disable();
    }

    public CoreTranslation getTranslation() {
        return translation;
    }

    public FunctionManager getFunction() {
        return function;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private void loadListeners() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new PluginListener(this), this);
        manager.registerEvents(new LoginListener(this), this);
        manager.registerEvents(new ChatListener(this), this);
        manager.registerEvents(new PingListener(this), this);
        manager.registerEvents(new JoinListener(this), this);
        manager.registerEvents(new QuitListener(this), this);
        manager.registerEvents(new KickListener(this), this);
        manager.registerEvents(new DeathListener(this), this);
    }

    private void loadCommands() {
        this.getCommand("start").setExecutor(new StartCommand(this));
        this.getCommand("spectate").setExecutor(new SpectateCommand(this));
        this.getCommand("maxplayers").setExecutor(new MaxPlayersCommand(this));
    }
}
