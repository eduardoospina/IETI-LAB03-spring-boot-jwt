package IETI.Lab1User.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class User{
    @Id
    String id;
    String name;
    @Indexed(unique = true)
    String email;
    String lastName;
    String createdAt;

    public User(){
        this.id = String.valueOf((int)(Math.random()*5));
        this.createdAt = LocalDate.now().toString();
    }

    public User(String name, String email, String lastName){
        this();
        this.name = name;
        this.email = email;
        this.lastName = lastName;
    }

    public User(String id,String name, String email, String lastName, String createdAt){
        this(name, email, lastName);
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
}
