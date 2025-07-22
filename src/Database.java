package latihan;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public List<ServiceType> getAllServiceTypes() {
        List<ServiceType> serviceTypes = new ArrayList<>();
        String sql = "SELECT * FROM service_types";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                serviceTypes.add(new ServiceType(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceTypes;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("service_type_id"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_type"),
                        rs.getString("license_plate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public Service addService(Service service) {
        String sql = "INSERT INTO services (description, amount, date, service_type_id, customer_name, vehicle_type, license_plate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, service.getDescription());
            pstmt.setBigDecimal(2, service.getAmount());
            pstmt.setDate(3, Date.valueOf(service.getDate()));
            pstmt.setInt(4, service.getserviceTypeId());
            pstmt.setString(5, service.getCustomerName());
            pstmt.setString(6, service.getVehicleType());
            pstmt.setString(7, service.getLicensePlate());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        service.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return service;
    }

    public Service updateService(Service service) {
        String sql = "UPDATE services SET description = ?, amount = ?, date = ?, service_type_id = ?, customer_name = ?, vehicle_type = ?, license_plate = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, service.getDescription());
            pstmt.setBigDecimal(2, service.getAmount());
            pstmt.setDate(3, Date.valueOf(service.getDate()));
            pstmt.setInt(4, service.getserviceTypeId());
            pstmt.setString(5, service.getCustomerName());
            pstmt.setString(6, service.getVehicleType());
            pstmt.setString(7, service.getLicensePlate());
            pstmt.setInt(8, service.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return service;
    }

    public void deleteService(int id) {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> getDailyReport() {
        Map<String, Double> report = new HashMap<>();
        String sql = "SELECT DATE(date) as report_date, SUM(amount) as total FROM services GROUP BY report_date";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                report.put(rs.getString("report_date"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    public Map<String, Double> getWeeklyReport() {
    Map<String, Double> report = new HashMap<>();
    String sql = "SELECT YEAR(date) AS year, WEEK(date, 1) AS week, SUM(amount) AS total " +
                 "FROM services GROUP BY year, week ORDER BY year, week";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            int year = rs.getInt("year");
            int week = rs.getInt("week");

            String formattedWeek = String.format("%d-W%02d", year, week);

            double total = rs.getDouble("total");
            report.put(formattedWeek, total);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return report;
}


    public Map<String, Double> getMonthlyReport() {
        Map<String, Double> report = new HashMap<>();
        String sql = "SELECT DATE_FORMAT(date, '%Y-%m') as report_month, SUM(amount) as total FROM services GROUP BY report_month";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                report.put(rs.getString("report_month"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    public Map<String, Double> getYearlyReport() {
        Map<String, Double> report = new HashMap<>();
        String sql = "SELECT YEAR(date) as report_year, SUM(amount) as total FROM services GROUP BY report_year";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                report.put(rs.getString("report_year"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
}
