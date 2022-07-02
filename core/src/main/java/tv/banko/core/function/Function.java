package tv.banko.core.function;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Function implements Listener {

    protected final JavaPlugin plugin;
    protected final String name;
    protected boolean status;

    public Function(JavaPlugin plugin, String name, boolean status) {
        this.plugin = plugin;
        this.name = name;
        this.status = status;

        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean isEnabled() {
        return status;
    }

    public boolean isDisabled() {
        return !status;
    }

    public void setStatus(boolean status) {
        this.status = status;

        if (status) {
            enable();
            return;
        }

        disable();
    }

    public Component getPrefix() {
        return Component.text(" ● ", NamedTextColor.DARK_GRAY)
                .append(Component.text(name, NamedTextColor.GREEN))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY));
    }

    public abstract void enable();
    public abstract void disable();

    public void load(YamlConfiguration config) {
        if (!config.contains(name.toLowerCase() + ".status")) {
            return;
        }

        this.status = config.getBoolean(name.toLowerCase() + ".status");
    }

    public void save(YamlConfiguration config) {
        config.set(name.toLowerCase() + ".status", status);
    }
}
