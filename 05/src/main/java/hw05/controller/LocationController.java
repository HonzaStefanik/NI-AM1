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
@RequestMapping(value = "/location")
public class LocationController {

    private final AppRepository repository;

    public LocationController(AppRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public CollectionModel<Location> getLocations() {
        List<Location> locations = repository.getLocations();
        CollectionModel<Location> response = CollectionModel.of(locations);
        for (Location location : locations) {
            location.removeLinks();
            location.add(linkTo(LocationController.class).slash(location.getId()).withRel("DETAIL"));
        }
        response.add(linkTo(LocationController.class).withRel("CREATE"));
        return response;
    }

    @GetMapping("/{id}")
    public HttpEntity<Location> getLocation(@PathVariable String id) throws NotFoundException {
        Location location = repository.getLocationById(id);
        location.removeLinks();
        location.add(linkTo(LocationController.class).slash(id).withSelfRel());
        location.add(linkTo(LocationController.class).slash(id).withRel("DELETE"));
        location.add(linkTo(LocationController.class).withRel("LIST"));
        location.add(linkTo(LocationController.class).withRel("CREATE"));
        location.add(linkTo(LocationController.class).withRel("UPDATE"));
        location.add(linkTo(CountryController.class).slash(location.getCountryId()).withRel("LOCATION_COUNTRY"));
        return ResponseEntity.status(HttpStatus.OK).body(location);
    }

    @PostMapping
    public ResponseEntity createLocation(@RequestBody Location location) {
        repository.addLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/location/" + location.getId()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateLocation(@PathVariable String id, @RequestBody Location location)  {
        try {
            repository.getLocationById(id);
            repository.updateLocation(location);
        } catch (NotFoundException e) {
            return createLocation(location);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Location", "/location/" + location.getId()).build();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteLocation(@PathVariable String id) throws NotFoundException {
        repository.deleteLocation(id);
    }
}
