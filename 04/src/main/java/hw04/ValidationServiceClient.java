package hw04;

import cz.cvut.fit.niam1.webservices.client.ValidateCardRequest;
import cz.cvut.fit.niam1.webservices.client.ValidateCardResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class ValidationServiceClient extends WebServiceGatewaySupport {

    public boolean validateCard(String cardOwner, String cardNumber) {
        ValidateCardRequest request = new ValidateCardRequest();
        request.setCardOwner(cardOwner);
        request.setCardNumber(cardNumber);
        ValidateCardResponse response = (ValidateCardResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response.isResult();
    }
}
