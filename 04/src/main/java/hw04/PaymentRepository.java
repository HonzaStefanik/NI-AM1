package hw04;

import cz.cvut.fit.niam1.webservices.client.Payment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentRepository {

    private static final List<Payment> payments = new ArrayList<>();

    public void addBooking(Payment booking) {
        // allow only unique IDs to simulate DB constraints
        for (Payment currentBooking : payments) {
            if (currentBooking.getId() == booking.getId()) {
                return;
            }
        }
        payments.add(booking);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void deletePayment(int id) {
        payments.removeIf(payment -> payment.getId() == id);
    }

}
