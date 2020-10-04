package hw01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@SpringBootTest(classes = TransformationController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransformationControllerTest {

    @LocalServerPort
    private int port;

    private URL url;

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void setUp() throws MalformedURLException {
        this.url = new URL("http://localhost:" + this.port + "/transform");

    }


    @Test
    public void testValidData() throws JsonProcessingException {
        HttpEntity<String> requestEntity = new HttpEntity<>(TestEntities.getValidInput());
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(
                new OutputFormat("1", "Prague", "Jan", "Stefanik")
        );

        ResponseEntity<String> response = template.postForEntity(
                url.toString(),
                requestEntity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedJson, response.getBody());

    }

    @Test
    public void testInvalidData() {
        HttpEntity<String> requestEntity = new HttpEntity<>(TestEntities.getInvalidInput());

        ResponseEntity<String> response = template.postForEntity(
                url.toString(),
                requestEntity,
                String.class
        );

        assertEquals(400, response.getStatusCodeValue());
    }
}
