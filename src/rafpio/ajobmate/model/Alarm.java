package rafpio.ajobmate.model;


public class Alarm {
    private long id;
    private long taskId;
    private long alarmTime;

    public Alarm(long Id, long Taskid, long Time) {
        id = Id;
        taskId = Taskid;
        alarmTime = Time;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
