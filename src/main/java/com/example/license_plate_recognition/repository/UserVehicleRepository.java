package com.example.license_plate_recognition.repository;



import com.example.license_plate_recognition.model.User;
import com.example.license_plate_recognition.model.UserVehicle;
import com.example.license_plate_recognition.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVehicleRepository extends JpaRepository<UserVehicle, Long> {
    List<UserVehicle> findByUser(User user);
    List<UserVehicle> findByVehicle(Vehicle vehicle);
    
    @Query("SELECT uv.vehicle FROM UserVehicle uv WHERE uv.user.id = :userId")
    List<Vehicle> findVehiclesByUserId(Long userId);
}
