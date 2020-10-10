package hw03;

import localhost.Booking;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingServiceRepository {

    private static final List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        // allow only unique IDs to simulate DB constraints
        for (Booking currentBooking : bookings) {
            if (currentBooking.getId() == booking.getId()) {
                return;
            }
        }
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void updateBooking(Booking updatedBooking) {
        for (Booking currentBooking : bookings) {
            if (currentBooking.getId() == updatedBooking.getId()) {
                currentBooking.setDepartureAirport(updatedBooking.getDepartureAirport());
                currentBooking.setArrivalAirport(updatedBooking.getArrivalAirport());
                currentBooking.setPassenger(updatedBooking.getPassenger());
                return;
            }
        }
    }

    public void deleteBooking(int id) {
        bookings.removeIf(flight -> flight.getId() == id);
    }

}

