package tv.banko.core.game;

public class GameTime {

    private final Game game;

    private Type type;
    private int time;

    public GameTime(Game game) {
        this.game = game;
        this.type = Type.PAUSED;
        this.time = 0;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void task() {
        switch (this.type) {
            case ASCENDING -> this.time++;
            case DESCENDING -> {
                this.time--;

                if (this.time == 0) {
                    game.nextState();
                }
            }
        }
    }

    public enum Type {

        ASCENDING,
        DESCENDING,
        PAUSED

    }

}
