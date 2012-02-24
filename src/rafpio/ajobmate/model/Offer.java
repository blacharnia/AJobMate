package rafpio.ajobmate.model;

public class Offer {
    private long id;
    private String position;
    private String employer;
    private String phoneNr;
    private String email;
    private String location;
    private String description;
    private double latitude;
    private double longitude;
    private boolean archive;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmployer() {
        return employer;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Offer(long id, String position, String employer, String phoneNr,
            String email, String location, String description, double latitude,
            double longitude, boolean archive) {

        this.id = id;
        this.location = location;
        this.position = position;
        this.employer = employer;
        this.phoneNr = phoneNr;
        this.email = email;
        this.location = location;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.archive = archive;
    }

    public Offer() {
    }

    public Offer copy() {
        Offer copy = new Offer();
        copy.location = location;
        copy.position = position;
        copy.employer = employer;
        copy.phoneNr = phoneNr;
        copy.email = email;
        copy.location = location;
        copy.description = description;
        copy.latitude = latitude;
        copy.longitude = longitude;
        copy.archive = archive;

        return copy;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isArchive() {
        return archive;
    }
}
