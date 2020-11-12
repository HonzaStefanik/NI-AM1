package hw06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tour")
@Slf4j
public class TourController {

    private List<Tour> tours = new ArrayList<>();
    private List<DeleteStatus> requestStatuses = new ArrayList<>();

    public TourController() {
        tours.add(new Tour(0, "first tour"));
        tours.add(new Tour(1, "second tour"));
        tours.add(new Tour(2, "third tour"));
        tours.add(new Tour(3, "fourth tour"));
        tours.add(new Tour(4, "fifth tour"));
        tours.add(new Tour(5, "sixth tour"));
        tours.add(new Tour(6, "seventh tour"));
        tours.add(new Tour(7, "eighth tour"));
        tours.add(new Tour(8, "ninth tour"));
        tours.add(new Tour(9, "tenth tour"));
    }

    @GetMapping
    public List<Tour> getTours() {
        return tours;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTour(@PathVariable int id) {
        Optional<Tour> toBeDeleted = tours.stream().filter(t -> t.getId() == id).findFirst();
        Optional<DeleteStatus> inProgress = requestStatuses.stream().filter(t -> t.getTourId() == id).findFirst();
        if (toBeDeleted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour with id " + id + " was not found");
        }
        if (inProgress.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tour with id " + id + " is already in progress of being deleted");
        }
        log.info("Delete process started for tour id=" + id);
        new Thread(() -> deleteTour(toBeDeleted.get())).start();
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/tour/delete-status/" + id).build();
    }

    @GetMapping(value = "/delete-status")
    public List<DeleteStatus> getDeleteStatuses() {
        return requestStatuses;
    }

    @GetMapping(value = "/delete-status/{id}")
    public ResponseEntity getDeleteStatuses(@PathVariable int id) {
        Optional<DeleteStatus> status = requestStatuses.stream().filter(t -> t.getTourId() == id).findFirst();
        if (status.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour with id " + id + " was not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(status.get());
    }

    void deleteTour(Tour t) {
        DeleteStatus status = new DeleteStatus(t.getId());
        requestStatuses.add(status);
        try {
            long timeout = 10000;
            log.info("Simulating waiting process for " + timeout + " ms");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // nothing
        }
        tours.removeIf(it -> it.getId() == status.getTourId());
        status.setStatus(DeleteStatus.Status.DELETED);
        log.info("Delete process finished for tour id=" + status.getTourId());
    }
}

