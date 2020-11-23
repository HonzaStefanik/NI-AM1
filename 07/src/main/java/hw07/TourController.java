package hw07;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tour")
@Slf4j
public class TourController {

    // most of the code will be in the controller class since it's only 1 entity class
    // with mostly simple logic; not the prettiest but it's ok for an assignment of this size

    private static List<Tour> tours = new ArrayList<>();
    private ZonedDateTime lastModified;

    public TourController() {
        tours.add(new Tour(0, "first tour"));
        tours.add(new Tour(1, "second tour", List.of(1, 2, 3)));
        tours.add(new Tour(2, "third tour", List.of(3, 4, 5)));
        lastModified = ZonedDateTime.now();
        logConditionalInfo();
    }

    @GetMapping
    public ResponseEntity getTours(
            @RequestHeader(value = "ETag", required = false) String requestEtagHeader,
            @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSinceHeader,
            @RequestParam(value = "strongEtag", required = false, defaultValue = "true") boolean strongEtag
    ) {
        logConditionalInfo(); // this is to make veryfing the task easier
        if (CacheHeaderUtil.isCacheUpToDate(requestEtagHeader, ifModifiedSinceHeader, lastModified, tours)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        String etag = strongEtag ?
                CacheHeaderUtil.calculateStrongEtagHeader(tours)
                : CacheHeaderUtil.calculateWeakEtagHeader(tours);
        String lastModifiedHeader = DateTimeFormatter.RFC_1123_DATE_TIME
                .withZone(ZoneId.of("GMT"))
                .format(lastModified);
        return ResponseEntity.status(HttpStatus.OK)
                .header("ETag", etag)
                .header("Last-Modified", lastModifiedHeader)
                .body(tours);
    }

    @PostMapping
    public ResponseEntity createTour(@RequestBody Tour tour) {
        tour.setId(tours.stream().mapToInt(Tour::getId).max().orElse(-1) + 1);
        tours.add(tour);
        lastModified = ZonedDateTime.now();
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/country/" + tour.getId()).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTour(@PathVariable int id) {
        Optional<Tour> toBeDeleted = tours.stream()
                .filter(tour -> tour.getId() == id)
                .findAny();
        if (toBeDeleted.isPresent()) {
            tours.remove(toBeDeleted.get());
            lastModified = ZonedDateTime.now();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateTour(@PathVariable int id, @RequestBody Tour tour) {
        Optional<Tour> toBeUpdated = tours.stream()
                .filter(t -> t.getId() == id)
                .findAny();
        if (toBeUpdated.isPresent()) {
            Tour t = toBeUpdated.get();
            // dont allow to update id
            t.setCustomers(tour.getCustomers());
            t.setDescription(tour.getDescription());
            lastModified = ZonedDateTime.now();
            return ResponseEntity.status(HttpStatus.OK).header("Location", "/country/" + tour.getId()).build();
        }
        return ResponseEntity.notFound().build();
    }

    private void logConditionalInfo() {
        log.info("Current strong ETag: " + CacheHeaderUtil.calculateStrongEtagHeader(tours));
        log.info("Current weak ETag: " + CacheHeaderUtil.calculateWeakEtagHeader(tours));
        log.info("Current Last-Modified value: " + DateTimeFormatter.RFC_1123_DATE_TIME
                .withZone(ZoneId.of("GMT"))
                .format(lastModified)
        );
    }
}
