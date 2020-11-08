package hw05.entity;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Tour extends RepresentationModel<Tour> {

    private int id;
    private String descriptions;
    private String locationId;
    private List<Integer> customers;

    public Tour(int id, String descriptions, String locationId) {
        this.id = id;
        this.descriptions = descriptions;
        this.locationId = locationId;
        this.customers = new ArrayList<>();
    }

    public Tour(int id, String descriptions, String locationId, List<Integer> customers) {
        this.id = id;
        this.descriptions = descriptions;
        this.locationId = locationId;
        this.customers = customers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<Integer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Integer> customers) {
        this.customers = customers;
    }
}
