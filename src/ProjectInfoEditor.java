import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ProjectInfoEditor {

    // Updated SQL statements with foreign keys
    private static final String UPDATE_PROJECT_SQL = "UPDATE PoisePMS SET Project_name=?, Building_type=?, Physical_address=?, " +
            "ERF_num=?, Total_fee=?, Total_paid=?, Project_deadline=? " +
            "WHERE Project_num = ?";

    private static final String UPDATE_CUSTOMER_SQL = "UPDATE Customer SET " +
            "Customer_name=?, Customer_cell_num=?, Customer_email=?, Customer_address=? " +
            "WHERE customer_id = (SELECT Customer_id FROM PoisePMS WHERE Project_num = ?)";

    private static final String UPDATE_ARCHITECT_SQL = "UPDATE Architect SET " +
            "Architect_name=?, Architect_cell_num=?, Architect_email=?, Architect_address=? " +
            "WHERE architect_id = (SELECT Architect_id FROM PoisePMS WHERE Project_num = ?)";

    private static final String UPDATE_CONTRACTOR_SQL = "UPDATE Contractor SET " +
            "Contractor_name=?, Contractor_cell_num=?, Contractor_email=?, Contractor_address=? " +
            "WHERE contractor_id = (SELECT Contractor_id FROM PoisePMS WHERE Project_num = ?)";

    private static final String SELECT_PROJECT_SQL = "SELECT * FROM PoisePMS WHERE Project_num = ?";

    /**
     * Checks if a project with the given project number exists in the database.
     *
     * @param connection The database connection.
     * @param projectNum The project number to check.
     * @return true if the project exists, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    private static boolean projectExists(Connection connection, int projectNum) throws SQLException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_PROJECT_SQL)) {
            selectStatement.setInt(1, projectNum);
            try (var resultSet = selectStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static void editProjectInfo(Connection connection, int projectNum) {
        try {
            // Check if the project exists before proceeding with editing
            if (!projectExists(connection, projectNum)) {
                System.out.println("Project not found for Project Number: " + projectNum);
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose the field to update for Project Number " + projectNum + ":");
            System.out.println("1. Project Information");
            System.out.println("2. Customer Information");
            System.out.println("3. Architect Information");
            System.out.println("4. Contractor Information");
            System.out.print("Enter your choice (1-4): ");
            int choice = getNumericUserInput(scanner, "Choice");

            switch (choice) {
                case 1:
                    updateProjectInformation(connection, scanner, projectNum);
                    break;
                case 2:
                    updateRelatedTable(connection, scanner, UPDATE_CUSTOMER_SQL, "Customer", projectNum);
                    break;
                case 3:
                    updateRelatedTable(connection, scanner, UPDATE_ARCHITECT_SQL, "Architect", projectNum);
                    break;
                case 4:
                    updateRelatedTable(connection, scanner, UPDATE_CONTRACTOR_SQL, "Contractor", projectNum);
                    break;
                default:
                    System.out.println("Invalid choice. No updates performed.");
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle more gracefully in a production environment
        }
    }

    private static void updateProjectInformation(Connection connection, Scanner scanner, int projectNum) throws SQLException {
        // Get updated information from the user
        String projectName = getUserInput(scanner, "Project Name");
        String buildingType = getUserInput(scanner, "Building Type");
        String physicalAddress = getUserInput(scanner, "Physical Address");
        int erfNumber = getNumericUserInput(scanner, "ERF Number");
        int totalFee = getNumericUserInput(scanner, "Total Fee");
        int totalPaid = getNumericUserInput(scanner, "Total Paid");
        String projectDeadline = getUserInput(scanner, "Project Deadline (YYYY-MM-DD)");

        // Set project_num and execute the update query for the PoisePMS table
        try (PreparedStatement updateProjectStatement = connection.prepareStatement(UPDATE_PROJECT_SQL)) {
            updateProjectStatement.setString(1, projectName);
            updateProjectStatement.setString(2, buildingType);
            updateProjectStatement.setString(3, physicalAddress);
            updateProjectStatement.setInt(4, erfNumber);
            updateProjectStatement.setInt(5, totalFee);
            updateProjectStatement.setInt(6, totalPaid);
            updateProjectStatement.setDate(7, java.sql.Date.valueOf(projectDeadline));
            updateProjectStatement.setInt(8, projectNum);

            int rowsUpdatedProject = updateProjectStatement.executeUpdate();

            if (rowsUpdatedProject > 0) {
                System.out.println("Project information updated successfully!");
            } else {
                System.out.println("Failed to update project information.");
            }
        }
    }

    /**
     * Gets numeric user input, validating for valid numbers.
     *
     * @param scanner The Scanner object for user input.
     * @param prompt   The prompt message.
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
     * @param prompt   The prompt message.
     * @return The user-provided input.
     */
    private static String getUserInput(Scanner scanner, String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine().trim(); // Trim to handle cases with only spaces
        return input;
    }

    // Modified method to handle foreign keys
    private static void updateRelatedTable(Connection connection, Scanner scanner,
                                           String updateSql, String tableName, int projectId) throws SQLException {
        System.out.println("Do you want to edit the existing " + tableName + " or assign a new " + tableName + "?");
        System.out.print("Enter 'edit' or 'new': ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("edit")) {
            updateExistingPerson(connection, scanner, updateSql, tableName, projectId);
        } else if (choice.equals("new")) {
            insertNewPerson(connection, scanner, tableName, projectId);
        } else {
            System.out.println("Invalid choice. No updates performed.");
        }
    }

    private static void updateExistingPerson(Connection connection, Scanner scanner,
                                             String updateSql, String tableName, int projectId) throws SQLException {
        System.out.println("Enter updated information for existing " + tableName +
                " with Project Number " + projectId + ":");

        String name = getUserInput(scanner, "Name");
        String cellNumber = getUserInput(scanner, "Cell Number");
        String email = getUserInput(scanner, "Email");
        String address = getUserInput(scanner, "Address");

        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setString(1, name);
            updateStatement.setString(2, cellNumber);
            updateStatement.setString(3, email);
            updateStatement.setString(4, address);
            updateStatement.setInt(5, projectId);

            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Existing " + tableName + " information updated successfully!");
            } else {
                System.out.println("Failed to update existing " + tableName + " information.");
            }
        }
    }

    private static void insertNewPerson(Connection connection, Scanner scanner, String tableName, int projectId) throws SQLException {
        System.out.println("Enter information for the new " + tableName + " to be assigned to Project Number " + projectId + ":");

        String name = getUserInput(scanner, "Name");
        String cellNumber = getUserInput(scanner, "Cell Number");
        String email = getUserInput(scanner, "Email");
        String address = getUserInput(scanner, "Address");

        String insertSql;
        switch (tableName) {
            case "Customer":
                insertSql = "INSERT INTO Customer (Customer_name, Customer_cell_num, Customer_email, Customer_address) VALUES (?, ?, ?, ?)";
                break;
            case "Architect":
                insertSql = "INSERT INTO Architect (Architect_name, Architect_cell_num, Architect_email, Architect_address) VALUES (?, ?, ?, ?)";
                break;
            case "Contractor":
                insertSql = "INSERT INTO Contractor (Contractor_name, Contractor_cell_num, Contractor_email, Contractor_address) VALUES (?, ?, ?, ?)";
                break;
            default:
                System.out.println("Invalid table name. No updates performed.");
                return;
        }

        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, name);
            insertStatement.setString(2, cellNumber);
            insertStatement.setString(3, email);
            insertStatement.setString(4, address);

            int rowsInserted = insertStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Get the generated key (person_id) and update the PoisePMS table with the new foreign key
                try (var generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newPersonId = generatedKeys.getInt(1);
                        updatePoisePMSForeignKey(connection, tableName, newPersonId, projectId);
                        System.out.println("New " + tableName + " assigned to Project Number " + projectId + " successfully!");
                    }
                }
            } else {
                System.out.println("Failed to insert new " + tableName + ". No updates performed.");
            }
        }
    }

    private static void updatePoisePMSForeignKey(Connection connection, String tableName, int newPersonId, int projectId) throws SQLException {
        String updateForeignKeySql = "UPDATE PoisePMS SET " + tableName.toLowerCase() + "_id = ? WHERE Project_num = ?";
        try (PreparedStatement updateForeignKeyStatement = connection.prepareStatement(updateForeignKeySql)) {
            updateForeignKeyStatement.setInt(1, newPersonId);
            updateForeignKeyStatement.setInt(2, projectId);
            updateForeignKeyStatement.executeUpdate();
        }
    }
}
