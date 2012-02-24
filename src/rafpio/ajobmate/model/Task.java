package rafpio.ajobmate.model;

public class Task {
    private long id;
    private String description;
    private long startTime;
    private long endTime;
    private long notificationTime;
    private long offerId;
    private String category;
    private boolean archive;

    public Task() {
    }

    public Task(long Id, String Description, long OfferId, long StartTime,
            long EndTime, long NotificationTime, String Category,
            boolean Archive) {
        id = Id;
        description = Description;
        offerId = OfferId;
        startTime = StartTime;
        endTime = EndTime;
        archive = Archive;
        category = Category;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
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
}
