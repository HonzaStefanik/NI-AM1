package hw01;

public class TestEntities {

    public static String getValidInput() {
        return new String(
          "Dear Sir or Madam,\n" +
                  "\n" +
                  "please find the details about my booking below:\n" +
                  "\n" +
                  "===\n" +
                  "Tour id: \"1\"\n" +
                  "Location: \"Prague\"\n" +
                  "Person: \"Jan Stefanik\"\n" +
                  "===\n" +
                  "\n" +
                  "Regards,\n" +
                  "Jan Stefanik"
        );
    }

    public static String getInvalidInput() {
        return new String(
                "Dear Sir or Madam,\n" +
                        "\n" +
                        "please find the details about my booking below:\n" +
                        "\n" +
                        "===\n" +
                        "Location: \"Bohemian Switzerland\"\n" +
                        "Person: \"Jan Novak\"\n" +
                        "===\n" +
                        "\n" +
                        "Regards,\n" +
                        "Jan Novak"
        );
    }
}
