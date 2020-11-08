package hw05.controller;

import hw05.entity.Country;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/tour")
public class TourController {

    private final AppRepository repository;

    public TourController(AppRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/")
    public CollectionModel<Tour> getTours(@RequestParam(value = "country", required = false) String countryId) {
        List<Tour> tours = repository.getTours();
        if (countryId != null) {
            try {
                repository.getCountryById(countryId);
            } catch (NotFoundException e) {
                return CollectionModel.empty();
            }
            tours = tours.stream().filter(it -> {
                try {
                    return repository.getCountryOfTour(it).getId().equals(countryId);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList());
        }
        CollectionModel<Tour> response = CollectionModel.of(tours);
        for (Tour tour : tours) {
            tour.removeLinks();
            tour.add(linkTo(TourController.class).slash(tour.getId()).withRel("DETAIL"));
        }
        return response;
    }

    @GetMapping("/{id}")
    public HttpEntity<Tour> getTour(@PathVariable int id) throws NotFoundException {
        Tour tour = repository.getTourById(id);
        tour.removeLinks();
        tour.add(linkTo(TourController.class).slash(id).withSelfRel());
        tour.add(linkTo(TourController.class).slash(id).withRel("DELETE"));
        tour.add(linkTo(TourController.class).withRel("LIST"));
        tour.add(linkTo(TourController.class).withRel("CREATE"));
        tour.add(linkTo(TourController.class).withRel("UPDATE"));
        tour.add(linkTo(TourController.class).slash(id).slash("customers").withRel("CUSTOMERS"));
        tour.add(linkTo(LocationController.class).slash(tour.getLocationId()).withRel("LOCATION"));
        return ResponseEntity.status(HttpStatus.OK).body(tour);
    }


    @PostMapping(value = "/")
    public ResponseEntity createTour(@RequestBody Tour tour) {
        repository.addTour(tour);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/country/" + tour.getId()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateTour(@PathVariable int id, @RequestBody Tour tour)  {
        try {
            repository.getTourById(id);
            repository.updateTour(tour);
        } catch (NotFoundException e) {
            return createTour(tour);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Location", "/country/" + tour.getId()).build();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTour(@PathVariable int id) throws NotFoundException {
        repository.deleteTour(id);
    }

    @GetMapping(value = "/{id}/customers")
    public CollectionModel<Customer> getTourCustomers(@PathVariable int id) throws NotFoundException {
        List<Customer> customers = repository.getCustomersByTourId(id);
        CollectionModel<Customer> response = CollectionModel.of(customers);
        for (Customer customer : customers) {
            customer.removeLinks();
            customer.add(linkTo(CustomerController.class).slash(customer.getId()).withRel("DETAIL"));
        }
        return response;
    }

}
