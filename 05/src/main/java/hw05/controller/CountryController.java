package hw05.controller;

import hw05.entity.Country;
import hw05.entity.Location;
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
@RequestMapping(value = "/country")
public class CountryController {

    private final AppRepository repository;

    public CountryController(AppRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public CollectionModel<Country> getCountries() {
        List<Country> countries = repository.getCountries();
        CollectionModel<Country> response = CollectionModel.of(countries);
        for (Country country : countries) {
            country.removeLinks();
            country.add(linkTo(CountryController.class).slash(country.getId()).withRel("DETAIL"));
        }
        response.add(linkTo(CountryController.class).withRel("CREATE"));
        return response;
    }

    @GetMapping("/{id}")
    public HttpEntity<Country> getCountry(@PathVariable String id) throws NotFoundException {
        Country country = repository.getCountryById(id);
        country.removeLinks();
        country.add(linkTo(CountryController.class).slash(id).withSelfRel());
        country.add(linkTo(CountryController.class).slash(id).withRel("DELETE"));
        country.add(linkTo(CountryController.class).withRel("LIST"));
        country.add(linkTo(CountryController.class).withRel("CREATE"));
        country.add(linkTo(CountryController.class).withRel("UPDATE"));
        country.add(linkTo(CountryController.class).slash(id).slash("locations").withRel("LOCATIONS"));
        return ResponseEntity.status(HttpStatus.OK).body(country);
    }

    @PostMapping(value = "/")
    public ResponseEntity createCountry(@RequestBody Country country) {
        repository.addCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/country/" + country.getId()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateCountry(@PathVariable String id, @RequestBody Country country) {
        try {
            repository.getCountryById(id);
            repository.updateCountry(country);
        } catch (NotFoundException e) {
            // country with this ID was not found; create it
            country.setId(id);
            return createCountry(country);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Location", "/country/" + country.getId()).build();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCountry(@PathVariable String id) throws NotFoundException {
        repository.deleteCountry(id);
    }

    @GetMapping(value = "/{id}/locations")
    public CollectionModel<Location> getLocations(@PathVariable String id) throws NotFoundException {
        List<Location> locations = repository.getLocationsByCountry(id);
        CollectionModel<Location> response = CollectionModel.of(locations);
        for (Location location : locations) {
            location.removeLinks();
            location.add(linkTo(LocationController.class).slash(location.getId()).withRel("DETAIL"));
        }
        return response;
    }

}


