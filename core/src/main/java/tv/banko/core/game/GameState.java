package tv.banko.core.game;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum GameState {

    WAITING(0),
    STARTING(1),
    RUNNING(2),
    FINISHED(3);

    private final int id;

    GameState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public GameState next() {
        return Arrays.stream(GameState.values()).filter(gameState -> gameState.getId() == (id + 1)).findFirst().orElse(null);
    }
}
