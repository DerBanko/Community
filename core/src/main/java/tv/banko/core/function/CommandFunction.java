package tv.banko.core.function;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandFunction extends Function implements CommandExecutor, TabCompleter {

    protected final String command;

    public CommandFunction(JavaPlugin plugin, String name, String command, boolean status) {
        super(plugin, name, status);
        this.command = command;

        PluginCommand pluginCommand = this.plugin.getCommand(command);

        if (pluginCommand == null) {
            throw new NullPointerException("Command '" + command + "' is not registered in Core.");
        }

        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }
}
