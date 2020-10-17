package hw04;

import cz.cvut.fit.niam1.webservices.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PaymentEndpoint {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private ValidationServiceClient validationServiceClient;

    @PayloadRoot(namespace = "http://localhost/", localPart = "getPaymentsRequest")
    @ResponsePayload
    public GetPaymentsResponse getBookings(@RequestPayload GetPaymentsRequest request) {
        GetPaymentsResponse response = new GetPaymentsResponse();
        response.getPayments().addAll(repository.getPayments());
        return response;
    }

    @PayloadRoot(namespace = "http://localhost/", localPart = "addPaymentRequest")
    @ResponsePayload
    public AddPaymentResponse addPayment(@RequestPayload AddPaymentRequest request) {
        AddPaymentResponse response = new AddPaymentResponse();
        String cardNumber = request.getPayment().getCardNumber();
        String cardOwner = request.getPayment().getCardOwner();
        if (validationServiceClient.validateCard(cardOwner, cardNumber)) {
            repository.addBooking(request.getPayment());
        }
        return response;
    }

    @PayloadRoot(namespace = "http://localhost/", localPart = "deletePaymentRequest")
    @ResponsePayload
    public DeletePaymentResponse deleteBooking(@RequestPayload DeletePaymentRequest request) {
        DeletePaymentResponse response = new DeletePaymentResponse();
        repository.deletePayment(request.getId());
        return response;
    }

}

