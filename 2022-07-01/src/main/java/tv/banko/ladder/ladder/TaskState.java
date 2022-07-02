package tv.banko.ladder.ladder;

import java.util.UUID;

public class TaskState {

    private final UUID uuid;
    private final int taskId;
    private Type type;
    private long reached;

    public TaskState(UUID uuid, int taskId) {
        this.uuid = uuid;
        this.taskId = taskId;
        this.type = Type.LOCKED;
        this.reached = 0;
    }

    public TaskState(UUID uuid, String s) {
        this.uuid = uuid;

        String[] strings = s.split("-");

        this.taskId = Integer.parseInt(strings[0]);
        this.type = Type.valueOf(strings[1]);
        this.reached = Long.parseLong(strings[2]);
    }

    public TaskState(UUID uuid, int taskId, Type type, long reached) {
        this.uuid = uuid;
        this.taskId = taskId;
        this.type = type;
        this.reached = reached;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getTaskId() {
        return taskId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;

        if (!this.type.equals(Type.REACHED)) {
            return;
        }

        this.reached = System.currentTimeMillis();
    }

    public long getReached() {
        return reached;
    }

    public boolean hasReached() {
        return type.equals(Type.REACHED);
    }

    @Override
    public String toString() {
        return taskId + "-" + type + "-" + reached;
    }

    public enum Type {
        LOCKED,
        TASK,
        REACHED
    }

}