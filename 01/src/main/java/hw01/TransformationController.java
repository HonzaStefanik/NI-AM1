package hw01;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class TransformationController {

    @PostMapping("/transform")
    public String transform(@RequestBody String data) throws ParsingException {
        return  TransformationUtil.transformData(data);
    }

    @ExceptionHandler(ParsingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleException(Exception e) {
        return e.getMessage();
    }
}
