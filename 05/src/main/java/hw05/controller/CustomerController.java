package hw05.controller;

import hw05.entity.Customer;
import hw05.entity.Tour;
import hw05.repository.AppRepository;
import hw05.repository.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    private final AppRepository repository;

    public CustomerController(AppRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public CollectionModel<Customer> getCustomers() {
        List<Customer> customers = repository.getCustomers();
        CollectionModel<Customer> response = CollectionModel.of(customers);
        for (Customer customer : customers) {
            customer.removeLinks();
            customer.add(linkTo(CustomerController.class).slash(customer.getId()).withRel("DETAIL"));
        }
        return response;
    }

    @GetMapping("/{id}")
    public HttpEntity<Customer> getCustomer(@PathVariable int id) throws NotFoundException {
        Customer customer = repository.getCustomersById(id);
        customer.removeLinks();
        customer.add(linkTo(CustomerController.class).slash(id).withSelfRel());
        customer.add(linkTo(CustomerController.class).slash(id).withRel("DELETE"));
        customer.add(linkTo(CustomerController.class).withRel("LIST"));
        customer.add(linkTo(CustomerController.class).withRel("CREATE"));
        customer.add(linkTo(CustomerController.class).withRel("UPDATE"));
        customer.add(linkTo(CustomerController.class).slash(id).slash("tours").withRel("TOURS"));
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody Customer customer) throws NotFoundException {
        List<Integer> tours = customer.getTours();
        for (Integer tourId : tours) {
            Tour t = repository.getTourById(tourId);
            t.getCustomers().add(customer.getId());
        }
        repository.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/customer/" + customer.getId()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateCustomer(@PathVariable int id, @RequestBody Customer customer) throws NotFoundException {
        try {
            repository.getCustomersById(id);
            repository.updateCustomer(customer);
        } catch (NotFoundException e) {
            return createCustomer(customer);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Location", "/customer/" + customer.getId()).build();

    }

    @DeleteMapping(value = "/{id}")
    public void deleteCustomer(@PathVariable int id) throws NotFoundException {
        Customer customer = repository.getCustomersById(id);
        List<Integer> tours = customer.getTours();
        for (Integer tourId : tours) {
            Tour t = repository.getTourById(tourId);
            t.getCustomers().add(customer.getId());
        }
        repository.deleteCustomer(id);
    }

    @GetMapping(value = "/{id}/tours")
    public CollectionModel<Tour> getCustomerTours(@PathVariable int id) throws NotFoundException {
        List<Tour> tours = repository.getToursByCustomerId(id);
        CollectionModel<Tour> response = CollectionModel.of(tours);
        for (Tour tour : tours) {
            tour.removeLinks();
            tour.add(linkTo(TourController.class).slash(tour.getId()).withRel("DETAIL"));
        }
        return response;
    }
}