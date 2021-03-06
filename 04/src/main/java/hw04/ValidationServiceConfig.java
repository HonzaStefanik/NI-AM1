package hw04;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ValidationServiceConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("cz.cvut.fit.niam1.webservices.client");
        return marshaller;
    }

    @Bean
    public ValidationServiceClient wsClient(Jaxb2Marshaller marshaller) {
        ValidationServiceClient client = new ValidationServiceClient();
        client.setDefaultUri("http://147.32.233.18:8888/NI-AM1-CardValidation/ws/card.wsdl");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}
