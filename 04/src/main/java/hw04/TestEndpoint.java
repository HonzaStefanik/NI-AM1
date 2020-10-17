package hw04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class TestEndpoint {

    @Autowired
    private ValidationServiceClient validationService;

    @GetMapping("/test")
    public String get(){
        validationService.validateCard("CardOwner", "1234-1234-1234-1234");
        return "test";
    }
}
