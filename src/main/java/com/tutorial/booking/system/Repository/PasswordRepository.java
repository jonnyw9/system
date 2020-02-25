package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRepository extends JpaRepository<Password, Integer> {
    //Optional<Password> findByUserPassId(int userPassId);


}
