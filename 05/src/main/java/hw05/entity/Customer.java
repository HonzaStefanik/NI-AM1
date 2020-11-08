package hw05.entity;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Customer extends RepresentationModel<Customer> {

    private Integer id;
    private String name;
    private String surname;
    private List<Integer> tours;

    public Customer(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tours = new ArrayList<>();
    }

    public Customer(Integer id, String name, String surname, List<Integer> tours) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tours = tours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Integer> getTours() {
        return tours;
    }

    public void setTours(List<Integer> tours) {
        this.tours = tours;
    }
}
