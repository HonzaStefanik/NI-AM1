package hw01;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFormat {
    @JsonProperty
    private final String id;
    @JsonProperty
    private final String location;
    @JsonProperty
    private final Person person;

    @JsonCreator
    public OutputFormat(String id, String location, Person person) {
        this.id = id;
        this.location = location;
        this.person = person;
    }

    @JsonCreator
    public OutputFormat(String id, String location, String name, String surname ) {
        this.id = id;
        this.location = location;
        this.person = new Person(name, surname);
    }

    public static class Person {
        @JsonProperty
        private final String name;
        @JsonProperty
        private final String surname;

        @JsonCreator
        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }
}
