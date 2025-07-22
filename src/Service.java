package latihan;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Service {
    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private int serviceTypeId;
    private String customerName;
    private String vehicleType;
    private String licensePlate;

    public Service(int id, String description, BigDecimal amount, LocalDate date, int serviceTypeId, String customerName, String vehicleType, String licensePlate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.serviceTypeId = serviceTypeId;
        this.customerName = customerName;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getserviceTypeId() {
        return serviceTypeId;
    }

    public void setserviceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
