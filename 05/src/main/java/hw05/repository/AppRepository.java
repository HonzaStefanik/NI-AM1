package hw05.repository;

import hw05.entity.Country;
import hw05.entity.Customer;
import hw05.entity.Location;
import hw05.entity.Tour;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppRepository {

    private static final List<Country> countries = new ArrayList<>();
    private static final List<Location> locations = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Tour> tours = new ArrayList<>();

    @PostConstruct
    public void initRepo() {
        countries.add(new Country("cz", "Czech Republic"));
        countries.add(new Country("de", "Germany"));
        locations.add(new Location("1", "Beach 1", "cz"));
        locations.add(new Location("2", "Beach 2", "cz"));
        locations.add(new Location("3", "Hotel", "de"));
        customers.add(new Customer(1, "firstCustomer", "01", List.of(1, 2)));
        customers.add(new Customer(2, "secondCustomer", "02", List.of(1)));
        tours.add(new Tour(1, "firstTour", "1", List.of(1, 2)));
        tours.add(new Tour(2, "secondTour", "2", List.of(1)));
        tours.add(new Tour(3, "thirdTour - initially empty", "3"));
    }

    public void addCountry(Country c) {
        if (countryExists(c.getId())) {
            return;
        }
        countries.add(c);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public Country getCountryById(String id) throws NotFoundException {
        return countries.stream().filter(c -> c.getId().equals(id)).findAny()
                .orElseThrow(() -> new NotFoundException("Country " + id + " does not exist"));
    }

    public void updateCountry(Country c) throws NotFoundException {
        Country toBeUpdated = countries.stream()
                .filter(it -> it.getId().equals(c.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Country " + c.getId() + " does not exist"));
        toBeUpdated.setName(c.getName());
    }

    public void deleteCountry(String countryId) throws NotFoundException {
        if (!countryExists(countryId)) {
            throw new NotFoundException("Country " + countryId + " does not exist");
        }
        List<Location> updateLocations = getLocationsByCountry(countryId);
        for (Location location : updateLocations) {
            deleteLocation(location);
        }
        countries.removeIf(c -> c.getId().equals(countryId));
    }

    public void addLocation(Location t) {
        if (locationExists(t.getId())) {
            return;
        }
        int max = locations.stream().map(l -> Integer.valueOf(l.getId())).max(Integer::compare).orElse(0);
        t.setId(String.valueOf(max + 1));
        locations.add(t);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Location> getLocationsByCountry(String countryId) throws NotFoundException {
        if (!countryExists(countryId)) {
            throw new NotFoundException("Country " + countryId + " does not exist");
        }
        return locations.stream()
                .filter(location -> location.getCountryId().equals(countryId))
                .collect(Collectors.toList());
    }

    public Location getLocationById(String locationId) throws NotFoundException {
        return locations.stream().filter(c -> c.getId().equals(locationId)).findAny()
                .orElseThrow(() -> new NotFoundException("Location " + locationId + " does not exist"));
    }

    public void updateLocation(Location l) throws NotFoundException {
        Location toBeUpdated = locations.stream()
                .filter(it -> it.getId().equals(l.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Location " + l.getId() + " does not exist"));
        toBeUpdated.setName(l.getName());
        toBeUpdated.setCountryId(l.getCountryId());
    }

    public void deleteLocation(Location location) throws NotFoundException {
        deleteLocation(location.getId());
    }

    public void deleteLocation(String locationId) throws NotFoundException {
        if (!locationExists(locationId)) {
            throw new NotFoundException("Location " + locationId + " does not exist");
        }
        List<Tour> updateTours = getToursByLocation(locationId);
        for(Tour t: updateTours) {
            t.setLocationId(null);
        }
        locations.removeIf(c -> c.getId().equals(locationId));
    }


    public void addCustomer(Customer c) {
        if (customerExists(c.getId())) {
            return;
        }
        int max = customers.stream().map(Customer::getId).max(Integer::compare).orElse(0);
        c.setId(max + 1);
        customers.add(c);
    }

    public void updateCustomer(Customer c) throws NotFoundException {
        Customer toBeUpdated = customers.stream()
                .filter(it -> it.getId() == c.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Customer " + c.getId() + " does not exist"));

        // get keys which are to be updated
        List<Integer> keyDifference = new ArrayList<>(CollectionUtils.disjunction(toBeUpdated.getTours(), c.getTours()));
        // update customer keys in tours
        for (Integer keyToUpdate : keyDifference) {
            Tour tourToUpdate = getTourById(keyToUpdate);
            if (tourToUpdate.getCustomers().contains(c.getId())) {
                tourToUpdate.getCustomers().remove(c.getId());
            } else {
                tourToUpdate.getCustomers().add(c.getId());
            }
        }
        toBeUpdated.setName(c.getName());
        toBeUpdated.setSurname(c.getSurname());
        toBeUpdated.setTours(c.getTours());
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomersById(int customerId) throws NotFoundException {
        return customers.stream().filter(c -> c.getId() == customerId).findAny()
                .orElseThrow(() -> new NotFoundException("Customer " + customerId + " does not exist"));
    }

    public List<Customer> getCustomersByTourId(int tourId) throws NotFoundException {
        if(!tourExists(tourId)){
            throw new NotFoundException("Tour " + tourId + " does not exist");
        }
        return customers.stream()
                .filter(c -> c.getTours().contains(tourId))
                .collect(Collectors.toList());
    }

    public void deleteCustomer(int customerId) throws NotFoundException {
        if (!customerExists(customerId)) {
            throw new NotFoundException("Customer " + customerId + " does not exist");
        }
        for (Tour t : tours) {
            t.getCustomers().removeIf(it -> it == customerId);
        }
        customers.removeIf(c -> c.getId() == customerId);
    }


    public void addTour(Tour t) {
        if (tourExists(t.getId())) {
            return;
        }
        int max = tours.stream().map(Tour::getId).max(Integer::compare).orElse(0);
        t.setId(max + 1);
        tours.add(t);
    }

    public void updateTour(Tour t) throws NotFoundException {
        Tour toBeUpdated = tours.stream()
                .filter(it ->  it.getId().equals(t.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Tour " + t.getId() + " does not exist"));

        // get keys which are to be updated
        List<Integer> keyDifference = new ArrayList<>(CollectionUtils.disjunction(toBeUpdated.getCustomers(), t.getCustomers()));
        // update tour keys in customers
        for (Integer keyToUpdate : keyDifference) {
            Customer customerToUpdate  = getCustomersById(keyToUpdate);
            if (customerToUpdate.getTours().contains(t.getId())) {
                customerToUpdate.getTours().remove(t.getId());
            } else {
                customerToUpdate.getTours().add(t.getId());
            }
        }
        toBeUpdated.setLocationId(t.getLocationId());
        toBeUpdated.setDescriptions(t.getDescriptions());
        toBeUpdated.setCustomers(t.getCustomers());
    }

    public List<Tour> getTours() {
        return tours;
    }

    public Tour getTourById(int tourId) throws NotFoundException {
        return tours.stream().filter(t -> t.getId() == tourId).findAny()
                .orElseThrow(() -> new NotFoundException("Tour " + tourId + " does not exist"));
    }

    public List<Tour> getToursByCustomerId(int customerId) throws NotFoundException {
        if (!customerExists(customerId)) {
            throw new NotFoundException("Tour " + customerId + " does not exist");
        }
        return tours.stream()
                .filter(t -> t.getCustomers().contains(customerId))
                .collect(Collectors.toList());
    }

    public List<Tour> getToursByLocation(String locationId) throws NotFoundException {
        if (!locationExists(locationId)) {
            throw new NotFoundException("Tour " + locationId + " does not exist");
        }
        return tours.stream()
                .filter(t -> t.getLocationId().equals(locationId))
                .collect(Collectors.toList());
    }

    public Country getCountryOfTour(Tour t) throws NotFoundException {
        Location tourLocation = null;
        for (Location location : locations) {
            if (location.getId().equals(t.getLocationId())) {
                tourLocation = location;
            }
        }
        return getCountryById(tourLocation.getCountryId());
    }

    public void deleteTour(int tourId) throws NotFoundException {
        if (!tourExists(tourId)) {
            throw new NotFoundException("Tour " + tourId + " does not exist");
        }
        for (Customer customer : customers) {
            customer.getTours().removeIf(it -> it == tourId);
        }
        tours.removeIf(c -> c.getId() == tourId);
    }


    private boolean countryExists(String id) {
        // allow only unique IDs to simulate DB constraints
        for (Country country : countries) {
            if (country.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean locationExists(String id) {
        // allow only unique IDs to simulate DB constraints
        for (Location location : locations) {
            if (location.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    private boolean customerExists(int id) {
        // allow only unique IDs to simulate DB constraints
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean tourExists(int id) {
        for (Tour tour : tours) {
            if (tour.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
