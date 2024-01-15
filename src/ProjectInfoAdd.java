import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * This class provides functionality to add a new project to the project management system.
 */
public class ProjectInfoAdd {

    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO Customer "
            + "(Customer_name, Customer_cell_num, Customer_email, Customer_address) "
            + "VALUES (?, ?, ?, ?)";

    private static final String INSERT_ARCHITECT_SQL = "INSERT INTO Architect "
            + "(Architect_name, Architect_cell_num, Architect_email, Architect_address) "
            + "VALUES (?, ?, ?, ?)";

    private static final String INSERT_CONTRACTOR_SQL = "INSERT INTO Contractor "
            + "(Contractor_name, Contractor_cell_num, Contractor_email, Contractor_address) "
            + "VALUES (?, ?, ?, ?)";

    private static final String INSERT_PROJECT_SQL = "INSERT INTO PoisePMS "
            + "(Project_num, Project_name, Building_type, Physical_address, ERF_num, "
            + "Total_fee, Total_paid, Project_deadline, Architect_id, Contractor_id, Customer_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Adds a new project to the database based on user input.
     *
     * @param connection The database connection.
     */
    public static void addNewProject(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter information for the new project:");

            // Get information from the user
            int projectNum = getNumericUserInput(scanner, "Project Number");
            String projectName = getUserInput(scanner, "Project Name");
            String buildingType = getUserInput(scanner, "Building Type");
            String physicalAddress = getUserInput(scanner, "Physical Address");
            int erfNumber = getNumericUserInput(scanner, "ERF Number");
            int totalFee = getNumericUserInput(scanner, "Total Fee");
            int totalPaid = getNumericUserInput(scanner, "Total Paid");
            String projectDeadline = getUserInput(scanner, "Project Deadline (YYYY-MM-DD)");

            // Get or insert data into Customer table
            int customerId = getOrCreatePersonId(scanner, connection, "Customer", INSERT_CUSTOMER_SQL);

            // Get or insert data into Architect table
            int architectId = getOrCreatePersonId(scanner, connection, "Architect", INSERT_ARCHITECT_SQL);

            // Get or insert data into Contractor table
            int contractorId = getOrCreatePersonId(scanner, connection, "Contractor", INSERT_CONTRACTOR_SQL);

            // Insert data into Project table
            insertIntoProject(connection, projectNum, projectName, buildingType, physicalAddress, erfNumber, totalFee, totalPaid, projectDeadline, architectId, contractorId, customerId);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle more gracefully in a production environment
        }
    }

    private static int getOrCreatePersonId(Scanner scanner, Connection connection, String personType, String insertSql) throws SQLException {
        System.out.println("Is this a new " + personType + "? (yes/no)");
        String isNewPerson = scanner.nextLine().trim().toLowerCase();

        if (isNewPerson.equals("yes")) {
            // If it's a new person, get information and insert into the respective table
            switch (personType) {
                case "Customer":
                    return insertIntoPerson(scanner, connection, INSERT_CUSTOMER_SQL);
                case "Architect":
                    return insertIntoPerson(scanner, connection, INSERT_ARCHITECT_SQL);
                case "Contractor":
                    return insertIntoPerson(scanner, connection, INSERT_CONTRACTOR_SQL);
                default:
                    throw new IllegalArgumentException("Invalid person type");
            }
        } else if (isNewPerson.equals("no")) {
            // If it's an existing person, ask for their ID
            System.out.println("Enter the " + personType + " ID:");
            return getNumericUserInput(scanner, personType + " ID");
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            return getOrCreatePersonId(scanner, connection, personType, insertSql);
        }
    }

    private static int insertIntoPerson(Scanner scanner, Connection connection, String insertSql) throws SQLException {
        System.out.println("Enter information for the new person:");

        String name = getUserInput(scanner, "Name");
        String cellNumber = getUserInput(scanner, "Cell Number");
        String email = getUserInput(scanner, "Email");
        String address = getUserInput(scanner, "Address");

        return insertIntoTable(connection, insertSql, name, cellNumber, email, address);
    }

    private static int insertIntoTable(Connection connection, String sql, String name, String cellNumber, String email, String address) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, name);
            insertStatement.setString(2, cellNumber);
            insertStatement.setString(3, email);
            insertStatement.setString(4, address);

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1; // Return -1 if the insertion failed
    }

    private static void insertIntoProject(Connection connection, int projectNum, String projectName, String buildingType, String physicalAddress,
                                          int erfNumber, int totalFee, int totalPaid, String projectDeadline,
                                          int architectId, int contractorId, int customerId) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_PROJECT_SQL)) {
            insertStatement.setInt(1, projectNum);
            insertStatement.setString(2, projectName);
            insertStatement.setString(3, buildingType);
            insertStatement.setString(4, physicalAddress);
            insertStatement.setInt(5, erfNumber);
            insertStatement.setInt(6, totalFee);
            insertStatement.setInt(7, totalPaid);
            insertStatement.setDate(8, java.sql.Date.valueOf(projectDeadline));
            insertStatement.setInt(9, architectId);
            insertStatement.setInt(10, contractorId);
            insertStatement.setInt(11, customerId);

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New project added successfully!");
            } else {
                System.out.println("Failed to add new project.");
            }
        }
    }

    // ... (previous code)

    /**
     * Gets numeric user input, validating for valid numbers.
     *
     * @param scanner The Scanner object for user input.
     * @param prompt  The prompt message.
     * @return The user-provided numeric input.
     */
    private static int getNumericUserInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int result = Integer.parseInt(scanner.nextLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Gets user input for a given prompt.
     *
     * @param scanner The Scanner object for user input.
     * @param prompt  The prompt message.
     * @return The user-provided input.
     */
    private static String getUserInput(Scanner scanner, String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine().trim(); // Trim to handle cases with only spaces
        return input;
    }
}
