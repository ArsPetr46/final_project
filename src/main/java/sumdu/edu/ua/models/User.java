package sumdu.edu.ua.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class User {
    @JsonProperty()
    private Integer userId;
    @JsonProperty()
    private String username;
    @JsonProperty()
    private String password;

    public User(Integer userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.password = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
