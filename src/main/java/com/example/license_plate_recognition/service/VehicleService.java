package com.example.license_plate_recognition.service;



import com.example.license_plate_recognition.dto.VehicleDto;
import com.example.license_plate_recognition.model.User;
import com.example.license_plate_recognition.model.UserVehicle;
import com.example.license_plate_recognition.model.Vehicle;
import com.example.license_plate_recognition.repository.UserRepository;
import com.example.license_plate_recognition.repository.UserVehicleRepository;
import com.example.license_plate_recognition.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final UserVehicleRepository userVehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository, UserVehicleRepository userVehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.userVehicleRepository = userVehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleRepository.existsByLicensePlate(licensePlate);
    }

    /**
     * Lấy danh sách xe dựa trên người dùng và vai trò
     * - Nếu là Admin: Lấy tất cả xe
     * - Nếu là Officer: Chỉ lấy xe mà người dùng đó quản lý
     */
    public List<Vehicle> getVehiclesByUserAndRole(Long userId, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return vehicleRepository.findAll();
        } else {
            return userVehicleRepository.findVehiclesByUserId(userId);
        }
    }

    /**
     * Thêm xe mới và liên kết với người dùng
     */
    @Transactional
    public Vehicle addVehicle(VehicleDto vehicleDto, Long userId) {
        // Kiểm tra biển số xe đã tồn tại chưa
        if (existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại");
        }

        // Lấy thông tin người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Tạo xe mới
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setOwnerName(vehicleDto.getOwnerName());
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setRegistrationDate(vehicleDto.getRegistrationDate());

        // Lưu xe vào database
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Tạo liên kết giữa người dùng và xe
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setUser(user);
        userVehicle.setVehicle(savedVehicle);
        userVehicleRepository.save(userVehicle);

        return savedVehicle;
    }

    /**
     * Cập nhật thông tin xe
     */
    @Transactional
    public Vehicle updateVehicle(Long id, VehicleDto vehicleDto) {
        // Kiểm tra xe tồn tại
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        // Kiểm tra biển số xe mới có trùng với xe khác không
        if (!vehicle.getLicensePlate().equals(vehicleDto.getLicensePlate()) && 
            existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new RuntimeException("Biển số xe mới đã tồn tại");
        }

        // Cập nhật thông tin xe
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setOwnerName(vehicleDto.getOwnerName());
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setRegistrationDate(vehicleDto.getRegistrationDate());

        return vehicleRepository.save(vehicle);
    }

    /**
     * Xóa xe
     */
    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
