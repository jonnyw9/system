package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
