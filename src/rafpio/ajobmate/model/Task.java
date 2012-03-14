package rafpio.ajobmate.model;

public class Task {
    private long id;
    private String description;
    private long startTime;
    private long endTime;
    private long notificationTime;
    private long offerId;
    private boolean archive;
    private int timeFlags;
    public static final int FLAG_START_TIME_SET = 1;
    public static final int FLAG_END_TIME_SET = 2;
    public static final int FLAG_ALARM_TIME_SET = 4;

    public Task() {
    }

    public Task(long Id, String Description, long OfferId, long StartTime,
            long EndTime, long NotificationTime, int TimeFlags, boolean Archive) {
        id = Id;
        description = Description;
        offerId = OfferId;
        startTime = StartTime;
        endTime = EndTime;
        archive = Archive;
        setTimeFlags(TimeFlags);
        notificationTime = NotificationTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public int getTimeFlags() {
        return timeFlags;
    }

    public void setTimeFlags(int timeFlags) {
        this.timeFlags = timeFlags;
    }

    public void enableFlag(int flagAlarmTimeSet) {
        this.timeFlags |= flagAlarmTimeSet;
    }

    public void disableFlag(int flagAlarmTimeSet) {
        this.timeFlags &= ~flagAlarmTimeSet;
    }

    public boolean isStartTimeSet() {
        return (timeFlags & FLAG_START_TIME_SET) == FLAG_START_TIME_SET;
    }

    public boolean isEndTimeSet() {
        return (timeFlags & FLAG_END_TIME_SET) == FLAG_END_TIME_SET;
    }

    public boolean isAlarmTimeSet() {
        return (timeFlags & FLAG_ALARM_TIME_SET) == FLAG_ALARM_TIME_SET;
    }

}
