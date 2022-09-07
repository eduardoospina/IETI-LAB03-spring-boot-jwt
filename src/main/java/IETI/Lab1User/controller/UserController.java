package IETI.Lab1User.controller;

import IETI.Lab1User.dto.UserDto;
import IETI.Lab1User.entities.User;
import IETI.Lab1User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping( "/api/user" )
public class UserController {
    private final UserService userService;
    private ModelMapper mapeo = new ModelMapper();

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(){
        List<UserDto> DTOusuarios = new ArrayList<>();
        List<User> listausuarios = userService.getAll();
        for (int i = 0; i< listausuarios.size(); i++){
            DTOusuarios.add(mapeo.map(listausuarios.get(i), UserDto.class));
        }
        try {
            return new ResponseEntity<>(DTOusuarios, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<UserDto> findById( @PathVariable String id ) {
        try{
            return new ResponseEntity<>(mapeo.map(userService.findById(id), UserDto.class), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto ) {
        try {
            User mapeouser = new User(userDto);
            userService.create(mapeouser);
            return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<UserDto> update( @RequestBody UserDto user, @PathVariable String id ) {
        try {
            userService.update(mapeo.map(user, User.class), id);
            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping( "/{id}" )
    @RolesAllowed("ADMIN")
    public ResponseEntity<Boolean> delete( @PathVariable String id ) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/Name/{textfind}")
    ResponseEntity<List<UserDto>> findUsersWithNameOrLastNameLike(@PathVariable String textfind) {
        try {
            List<User> usuarios = userService.findUsersWithNameOrLastNameLike(textfind);
            List<UserDto> listausuariosDTO = new ArrayList<>();
            for (int i = 0; i< usuarios.size(); i++){
                listausuariosDTO.add(mapeo.map(usuarios.get(i), UserDto.class));
            }
            return new ResponseEntity<>(listausuariosDTO, HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/creado/{createdAt}")
    public ResponseEntity<List<UserDto>> findUsersCreatedAfter(@PathVariable String createdAt){
        try {
            List<User> usuarios = userService.findUsersCreatedAfter(createdAt);
            List<UserDto> listausuariosDTOd = new ArrayList<>();
            for (int i = 0; i< usuarios.size(); i++){
                listausuariosDTOd.add(mapeo.map(usuarios.get(i), UserDto.class));
            }
            return new ResponseEntity<>(listausuariosDTOd, HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

