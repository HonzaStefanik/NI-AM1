package hw01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformationUtil {

    private static final String inputDataPattern = ".*" +
            "===\\n" +
            "Tour id: \"([0-9]+)\"\\r?\\n" +
            "Location: \"(.+)\"\\r?\\n" +
            "Person: \"([a-zA-Z]+).* ([a-zA-Z]+)\"\\r?\\n" +
            "===\\n" +
            ".*";


    private TransformationUtil() {
    }

    public static String transformData(String data) throws ParsingException {
        Matcher matcher = Pattern.compile(inputDataPattern, Pattern.DOTALL).matcher(data);
        if (!matcher.matches()) {
            throw new ParsingException("Received malformed input data.");
        }

        OutputFormat outputFormat = new OutputFormat(
                matcher.group(1),
                matcher.group(2),
                matcher.group(3),
                matcher.group(4)
        );

        try {
            return new ObjectMapper().writeValueAsString(outputFormat);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e.getMessage());
        }
    }
}
