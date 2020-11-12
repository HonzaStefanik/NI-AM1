package hw06;

public class Tour {
    private int id;
    private String descriptions;

    public Tour(int id, String descriptions) {
        this.id = id;
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

}
