package IETI.Lab1User.dto;

import java.time.LocalDate;

public class UserDto {
    private String id;
    private String name;
    private String email;
    private String lastName;
    private String createdAt;
    private String password;

    public UserDto(){
        this.id = String.valueOf((int)(Math.random()*9));
        this.createdAt = LocalDate.now().toString();

    }

    public UserDto(String name, String email, String lastName, String password){
        this();
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.password = password;
    }

    public UserDto(String id,String name, String email, String lastName, String createdAt, String passwords){
        this(name, email, lastName, passwords);
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
