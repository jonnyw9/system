package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
