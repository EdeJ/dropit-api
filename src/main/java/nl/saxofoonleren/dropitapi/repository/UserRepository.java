package nl.saxofoonleren.dropitapi.repository;

import nl.saxofoonleren.dropitapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}