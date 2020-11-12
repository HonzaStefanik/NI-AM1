package hw06;

public class DeleteStatus {

    private int tourId;
    private Status status;

    public DeleteStatus(int tourId) {
        this.tourId = tourId;
        this.status = Status.IN_PROGRESS;
    }

    public static enum Status {
        IN_PROGRESS("In progress"),
        DELETED("Deleted");

        public String stringValue;

        Status(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
