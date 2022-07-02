package tv.banko.core.function;

import tv.banko.core.Core;
import tv.banko.core.config.FunctionConfig;
import tv.banko.core.function.command.GlobalMute;
import tv.banko.core.function.command.SlowChat;
import tv.banko.core.function.command.Whitelist;
import tv.banko.core.translation.FunctionTranslation;

import java.util.ArrayList;
import java.util.List;

public class FunctionManager {

    private final Core core;

    private final FunctionTranslation translation;
    private final FunctionConfig config;

    private final List<Function> list;

    public FunctionManager(Core core) {
        this.core = core;
        this.translation = new FunctionTranslation();
        this.config = new FunctionConfig(core);

        this.list = new ArrayList<>();
    }

    public void enable() {
        this.list.add(new GlobalMute(core));
        this.list.add(new SlowChat(core));
        this.list.add(new Whitelist(core));

        loadAll();
    }

    public void disable() {
        saveAll();
    }

    public FunctionTranslation getTranslation() {
        return translation;
    }

    private void loadAll() {
        this.list.forEach(config::loadFunction);
    }

    private void saveAll() {
        this.list.forEach(config::saveFunction);
    }
}
