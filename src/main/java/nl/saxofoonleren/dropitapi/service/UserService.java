package nl.saxofoonleren.dropitapi.service;


import nl.saxofoonleren.dropitapi.exception.RecordNotFoundException;
import nl.saxofoonleren.dropitapi.model.User;
import nl.saxofoonleren.dropitapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addUser(User newUser) {
        if (!userRepository.existsByEmail(newUser.getEmail())) {

            User savedUser = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(500).body("E-mailaddress is not unique");
    }

    public User getUserByEmail(String email) {

        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {
            throw new RecordNotFoundException();
        }
    }
}
