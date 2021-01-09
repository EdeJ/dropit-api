package nl.saxofoonleren.dropitapi.controller;


import nl.saxofoonleren.dropitapi.UserModelAssembler;
import nl.saxofoonleren.dropitapi.exception.UserNotFoundException;
import nl.saxofoonleren.dropitapi.model.Order;
import nl.saxofoonleren.dropitapi.model.User;
import nl.saxofoonleren.dropitapi.repository.UserRepository;
import nl.saxofoonleren.dropitapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler assembler;
    private UserService userService;

    public UserController(UserRepository userRepository, UserModelAssembler assembler) {
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody User newUser) {

        return userService.addUser(newUser);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        User userToReturn = new User();
        List<Order> orders = user.getOrders();
        for (Order o : orders) {
            o.setUser(null);
        }
        userToReturn.setOrders(orders);
        userToReturn.setEmail(user.getEmail());


        return userToReturn;
    }


    @GetMapping(value = "/users/email/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(email, HttpStatus.OK);
    }



        @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable Long id) {

        User updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });

        EntityModel<User> entityModel = assembler.toModel(updatedUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }


}
