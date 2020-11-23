package hw07;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Tour {
    private int id;
    private String description;
    private List<Integer> customers;

    public Tour(int id, String description) {
        this.id = id;
        this.description = description;
        this.customers = new ArrayList<>();
    }

    public Tour(int id, String description, List<Integer> customers) {
        this.id = id;
        this.description = description;
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", customers=" + customers +
                '}';
    }
}
