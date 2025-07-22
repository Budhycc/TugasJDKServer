CREATE TABLE service_types (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE services (
  id INT AUTO_INCREMENT PRIMARY KEY,
  description VARCHAR(255) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  date DATE NOT NULL,
  service_type_id INT,
  customer_name VARCHAR(100),
  vehicle_type VARCHAR(50),
  license_plate VARCHAR(20),
  FOREIGN KEY (service_type_id) REFERENCES service_types(id)
);

INSERT INTO service_types (name) VALUES
('Ganti Oli'),
('Servis Rutin'),
('Ganti Ban'),
('Turun Mesin'),
('Lainnya');
