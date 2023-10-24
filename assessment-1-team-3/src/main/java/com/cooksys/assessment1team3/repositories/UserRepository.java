package com.cooksys.assessment1team3.repositories;

import com.cooksys.assessment1team3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // derived method
    User findByCredentialsUsername(String username);
}
