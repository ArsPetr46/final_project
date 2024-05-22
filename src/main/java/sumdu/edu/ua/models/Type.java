package sumdu.edu.ua.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Type {
    @JsonProperty()
    private Integer id;
    @JsonProperty()
    private String name;
    @JsonProperty()
    private String description;

    public Type(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
