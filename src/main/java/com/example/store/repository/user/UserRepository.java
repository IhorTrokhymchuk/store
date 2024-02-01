package com.example.store.repository.user;

import com.example.store.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN FETCH u.roles r WHERE u.email = :email")
    Optional<User> findUserByEmailWithRoles(String email);
}
