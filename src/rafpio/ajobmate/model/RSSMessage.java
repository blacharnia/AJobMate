package rafpio.ajobmate.model;

public class RSSMessage implements Comparable<RSSMessage> {
    public RSSMessage(long id, String title, String link, String date,
            String description) {
        super();

        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public RSSMessage() {
    }

    private String title;
    private String link;
    private String description;
    private String date;
    private long id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RSSMessage copy() {
        RSSMessage copy = new RSSMessage();
        copy.id = id;
        copy.title = title;
        copy.link = link;
        copy.description = description;
        copy.date = date;
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ");
        sb.append(title);
        sb.append('\n');
        sb.append("Date: ");
        sb.append(this.getDate());
        sb.append('\n');
        sb.append("Link: ");
        sb.append(link);
        sb.append('\n');
        sb.append("Description: ");
        sb.append(description);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RSSMessage other = (RSSMessage) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public int compareTo(RSSMessage another) {
        if (another == null)
            return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
