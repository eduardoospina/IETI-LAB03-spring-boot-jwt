package IETI.Lab1User.entities;

import IETI.Lab1User.entities.enums.RoleEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCrypt;
import IETI.Lab1User.dto.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
public class User{
    @Id
    String id;
    String name;
    @Indexed(unique = true)
    String email;
    String lastName;
    String createdAt;
    private String passwordHash;
    private List<RoleEnum> roles;


    public User(){
        this.id = String.valueOf((int)(Math.random()*5));
        this.createdAt = LocalDate.now().toString();
        this.roles = new ArrayList<>();
    }

    public User(String name, String email, String lastName, UserDto userdto){
        this();
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.passwordHash = BCrypt.hashpw(userdto.getPassword(), BCrypt.gensalt());
    }

    public User(String id, String name, String email, String lastName,UserDto userdto, String createdAt) {
        this(name, email, lastName, userdto);
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

    public String getPasswordHash(){
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }

    public void toEntity(UserDto user) {
        //Previous labs implementation
        if (user.getPassword() != null) {
            this.passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        }
    }



}
