package nl.saxofoonleren.dropitapi.controller;


import nl.saxofoonleren.dropitapi.UserModelAssembler;
import nl.saxofoonleren.dropitapi.exception.UserNotFoundException;
import nl.saxofoonleren.dropitapi.model.User;
import nl.saxofoonleren.dropitapi.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserRepository repository;
    private final UserModelAssembler assembler;

    public UserController(UserRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllUsers() {

        List<EntityModel<User>> users = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

//    @PostMapping("/users")
//    public User saveUser(@RequestBody User newUser) {
//        return repository.save(newUser);
//    }

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody User newUser) {

        EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {

        User user = repository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

//    @PutMapping("/users/{id}")
//    User replaceEmployee(@RequestBody User newUser, @PathVariable Long id) {
//
//        return repository.findById(id)
//                .map(user -> {
//                    user.setEmail(newUser.getEmail());
//                    user.setPassword(newUser.getPassword());
//                    return repository.save(user);
//                })
//                .orElseGet(() -> {
//                    newUser.setId(id);
//                    return repository.save(newUser);
//                });
//    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable Long id) {

        User updatedUser = repository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });

        EntityModel<User> entityModel = assembler.toModel(updatedUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }


}
