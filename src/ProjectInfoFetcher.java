import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides functionality to fetch project information from the project management system.
 */
public class ProjectInfoFetcher {

    /**
     * Fetches project information based on the provided project number.
     *
     * @param connection The database connection.
     * @param projectNum The project number to fetch information for.
     * @throws SQLException If a database error occurs.
     */
    public static void fetchProjectInfo(Connection connection, int projectNum) throws SQLException {
        String selectProjectSQL = "SELECT * FROM PoisePMS "
                + "JOIN Customer ON PoisePMS.Customer_id = Customer.Customer_id "
                + "JOIN Architect ON PoisePMS.Architect_id = Architect.Architect_id "
                + "JOIN Contractor ON PoisePMS.Contractor_id = Contractor.Contractor_id "
                + "WHERE Project_num = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectProjectSQL)) {
            preparedStatement.setInt(1, projectNum);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    boolean isFinalized = resultSet.getBoolean("Finalized");

                    // Display project details collected from the database
                    System.out.println("=======================");
                    System.out.println("Project Details:");

                    // Print project information
                    printProjectInformation(resultSet);

                    // Print related table information
                    System.out.println("Customer Details:");
                    printRelatedTableInformation(resultSet, "Customer");

                    System.out.println("Architect Details:");
                    printRelatedTableInformation(resultSet, "Architect");

                    System.out.println("Contractor Details:");
                    printRelatedTableInformation(resultSet, "Contractor");

                    // Print project finalization and completion information
                    System.out.println("Finalized: " + (isFinalized ? "true" : "false"));
                    System.out.println("Date of Completion: " + resultSet.getString("Completion_date"));
                    System.out.println("=======================");
                } else { // Error handling
                    System.out.println("Project not found for Project Number: " + projectNum);
                }
            }
        }
    }

    /**
     * Prints information for a specific project.
     *
     * @param resultSet The result set containing project information.
     * @throws SQLException If a database error occurs.
     */
    private static void printProjectInformation(ResultSet resultSet) throws SQLException {
        // Print project information
        System.out.println("Project Number: " + resultSet.getInt("Project_num"));
        System.out.println("Project Name: " + resultSet.getString("Project_name"));
        System.out.println("Building Type: " + resultSet.getString("Building_type"));
        // Print other project information
        System.out.println("Physical Address: " + resultSet.getString("Physical_address"));
        System.out.println("ERF Number: " + resultSet.getInt("ERF_num"));
        System.out.println("Total Fee: " + resultSet.getInt("Total_fee"));
        System.out.println("Total Paid: " + resultSet.getInt("Total_paid"));
        System.out.println("Project Deadline: " + resultSet.getDate("Project_deadline"));
    }

    /**
     * Prints information for a related table (Customer, Architect, or Contractor).
     *
     * @param resultSet The result set containing related table information.
     * @param tableName The name of the related table.
     * @throws SQLException If a database error occurs.
     */
    private static void printRelatedTableInformation(ResultSet resultSet, String tableName) throws SQLException {
        System.out.println(tableName + " Name: " + resultSet.getString(tableName + "_name"));
        System.out.println(tableName + " Cell Number: " + resultSet.getString(tableName + "_cell_num"));
        System.out.println(tableName + " Email: " + resultSet.getString(tableName + "_email"));
        System.out.println(tableName + " Address: " + resultSet.getString(tableName + "_address"));
    }

    /**
     * Finds projects that are not finalized.
     *
     * @param connection The database connection.
     */
    public static void findIncompleteProjects(Connection connection) {
        try {
            String query = "SELECT * FROM PoisePMS WHERE finalized IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        // Print project information
                        System.out.println("Project Number: " + resultSet.getInt("project_num"));
                        System.out.println("Project Name: " + resultSet.getString("project_name") + "\n");
                        // Print other project information
                    } while (resultSet.next());
                } else {
                    System.out.println("No projects need to be completed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds projects that have a  completion date in the past.
     *
     * @param connection The database connection.
     */
    public static void findOverdueProjects(Connection connection) {
        try {
            // Get the current date
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            // Select all projects that have a date less then the current date
            String query = "SELECT * FROM PoisePMS WHERE Project_deadline < ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, currentDate);
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    do {
                        // Print project information
                        System.out.println("\n"  +"Project Number: " + resultSet.getInt("Project_num"));
                        System.out.println("Project Name: " + resultSet.getString("Project_name"));
                        System.out.println("Project Deadline: " + resultSet.getString("Project_deadline") + "\n");
                    } while (resultSet.next());
                } else {
                    System.out.println("\nNo need to be completed.\n\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 
}
