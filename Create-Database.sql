-- // ================= Create Architect Table ================= //

CREATE TABLE Architect (
  Architect_id INT AUTO_INCREMENT PRIMARY KEY,
  Architect_name VARCHAR(255) NOT NULL,
  Architect_cell_num VARCHAR(20) NOT NULL,
  Architect_email VARCHAR(255) NOT NULL,
  Architect_address VARCHAR(255) NOT NULL
);


-- // ================= Create Contractor Table ================= //

CREATE TABLE Contractor (
  Contractor_id INT AUTO_INCREMENT PRIMARY KEY,
  Contractor_name VARCHAR(255) NOT NULL,
  Contractor_cell_num VARCHAR(20) NOT NULL,
  Contractor_email VARCHAR(255) NOT NULL,
  Contractor_address VARCHAR(255) NOT NULL
);

-- // ================= Create Customer Table ================= //

CREATE TABLE Customer (
  Customer_id INT AUTO_INCREMENT PRIMARY KEY,
  Customer_name VARCHAR(255) NOT NULL,
  Customer_cell_num VARCHAR(20) NOT NULL,
  Customer_email VARCHAR(255) NOT NULL,
  Customer_address VARCHAR(255) NOT NULL
);

 -- // ================= Create PoisePMS Table ================= //

CREATE TABLE PoisePMS (
    Project_num INT PRIMARY KEY,
    Project_name VARCHAR(255),
    Building_type VARCHAR(255),
    Physical_address VARCHAR(255),
    ERF_num INT,
    Total_fee INT,
    Total_paid INT,
    Project_deadline DATE,
    Architect_id INT,
    Contractor_id INT,
    Customer_id INT,
    Finalized BOOLEAN,
    Completion_date DATE,
    FOREIGN KEY (Architect_id) REFERENCES Architect(Architect_id),
    FOREIGN KEY (Contractor_id) REFERENCES Contractor(Contractor_id),
    FOREIGN KEY (Customer_id) REFERENCES Customer(Customer_id)
);

-- -- // ================= Insert Values ================= //


INSERT INTO Architect (Architect_name, Architect_cell_num, Architect_email, Architect_address)
VALUES
('John Doe', '123-456-7890', 'john.doe@example.com', '123 Main St, Cape Town'),
('Jane Smith', '987-654-3210', 'jane.smith@example.com', '456 Oak Ave, Durban'),
('Bob Johnson', '555-123-4567', 'bob.johnson@example.com', '789 Elm St, Johannesburg');



INSERT INTO Contractor (Contractor_name, Contractor_cell_num, Contractor_email, Contractor_address)
VALUES
('Mike Anderson', '111-222-3333', 'mike.anderson@example.com', '101 Maple Blvd, Pretoria'),
('Sara Brown', '444-555-6666', 'sara.brown@example.com', '202 Pine St, Bloemfontein'),
('Chris Wilson', '777-888-9999', 'chris.wilson@example.com', '303 Cedar Ave, Port Elizabeth');



INSERT INTO Customer (Customer_name, Customer_cell_num, Customer_email, Customer_address)
VALUES
('Alice Johnson', '111-333-5555', 'alice.johnson@example.com', '404 Birch St, East London'),
('David Miller', '444-666-8888', 'david.miller@example.com', '505 Oak Ave, Pietermaritzburg'),
('Karen Lee', '777-999-1111', 'karen.lee@example.com', '606 Pine Blvd, Nelspruit');



INSERT INTO PoisePMS (Project_num, Project_name, Building_type, Physical_address, ERF_num, Total_fee, Total_paid, Project_deadline, Architect_id, Contractor_id, Customer_id)
VALUES
(1, 'Green Valley Residence', 'Residential', '123 Main St', 456789, 50000, 20000, '2024-12-31', 1, 1, 1),
(2, 'Skyline Tower', 'Commercial', '456 Oak Ave', 987654, 80000, 30000, '2025-06-30', 2, 2, 2),
(3, 'Tech Park Expansion', 'Industrial', '789 Pine Rd', 123456, 120000, 60000, '2025-09-15', 3, 3, 3);
