package tv.banko.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamePlayers {

    private final List<UUID> players;
    private final List<UUID> spectators;

    public GamePlayers() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
    }

    public void addSpectator(UUID uuid) {
        if (isSpectator(uuid)) {
            return;
        }

        this.spectators.add(uuid);
    }

    public void removeSpectator(UUID uuid) {
        this.spectators.remove(uuid);
    }

    public boolean isSpectator(UUID uuid) {
        return this.spectators.contains(uuid);
    }

    public void addPlayer(UUID uuid) {
        if (isPlayer(uuid)) {
            return;
        }

        this.players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public boolean isPlayer(UUID uuid) {
        return this.players.contains(uuid);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }
}
