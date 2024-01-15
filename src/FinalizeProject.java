import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides functionality to finalize existing projects in the project management system.
 */
public class FinalizeProject {

    /**
     * Finalizes an existing project by marking it as finalized and adding the completion date.
     *
     * @param connection The database connection.
     * @param projectNum The project number to be finalized.
     */
    public static void finalizeExistingProject(Connection connection, int projectNum) {
        try {
            // Check if the project exists before finalizing
            if (isProjectExists(connection, projectNum)) {
                // Check if the project is already finalized
                if (!isProjectFinalized(connection, projectNum)) {
                    // Finalize the project
                    markProjectAsFinalized(connection, projectNum);
                    addCompletionDate(connection, projectNum);
                    System.out.println("Project finalized successfully.");
                } else {
                    System.out.println("Project is already finalized.");
                }
            } else {
                System.out.println("Project not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a project with the given project number exists in the database.
     *
     * @param connection The database connection.
     * @param projectNum The project number to check.
     * @return true if the project exists, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    private static boolean isProjectExists(Connection connection, int projectNum) throws SQLException {
        String query = "SELECT * FROM PoisePMS WHERE project_num = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectNum);
            return preparedStatement.executeQuery().next();
        }
    }

    /**
     * Checks if a project with the given project number is already finalized.
     *
     * @param connection The database connection.
     * @param projectNum The project number to check.
     * @return true if the project is finalized, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    private static boolean isProjectFinalized(Connection connection, int projectNum) throws SQLException {
        String query = "SELECT * FROM PoisePMS WHERE project_num = ? AND finalized = true";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectNum);
            return preparedStatement.executeQuery().next();
        }
    }

    /**
     * Marks a project as finalized in the database.
     *
     * @param connection The database connection.
     * @param projectNum The project number to mark as finalized.
     * @throws SQLException If a database error occurs.
     */
    private static void markProjectAsFinalized(Connection connection, int projectNum) throws SQLException {
        String query = "UPDATE PoisePMS SET Finalized = true WHERE project_num = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectNum);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Adds the completion date to a project in the database.
     *
     * @param connection The database connection.
     * @param projectNum The project number to add the completion date to.
     * @throws SQLException If a database error occurs.
     */
    private static void addCompletionDate(Connection connection, int projectNum) throws SQLException {
        String query = "UPDATE PoisePMS SET Completion_date = ? WHERE project_num = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, getCurrentDate());
            preparedStatement.setInt(2, projectNum);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Gets the current date in the "yyyy-MM-dd" format.
     *
     * @return The current date as a formatted string.
     */
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}
