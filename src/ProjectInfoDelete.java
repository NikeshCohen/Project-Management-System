import java.sql.*;

/**
 * This class provides functionality to delete project information from the project management system.
 */
public class ProjectInfoDelete {

    /**
     * Deletes project information based on the specified project ID.
     *
     * @param connection The Connection object for the database connection.
     * @param projectId  The project ID to be deleted.
     * @throws SQLException If a database error occurs.
     */
    public static void deleteInfo(Connection connection, int projectId) throws SQLException {
        try {
            // Disable auto-commit to ensure atomicity of the delete operations
            connection.setAutoCommit(false);

            // Delete information from all related tables
            deleteFromTable(connection, "PoisePMS", "project_num", projectId); // Project table
            deleteFromTable(connection, "Customer", "customer_id", projectId); // Customer table
            deleteFromTable(connection, "Architect", "architect_id", projectId); // Architect table
            deleteFromTable(connection, "Contractor", "contractor_id", projectId); // Contractor table

            // Commit the transaction if all delete operations succeed
            connection.commit();

            // Display a confirmation message
            System.out.println("Project with project_num " + projectId + " and related information has been deleted.");

        } catch (SQLException e) {
            // Rollback the transaction in case of any exceptions
            connection.rollback();
            throw e;
        } finally {
            // Enable auto-commit to return to the default behavior
            connection.setAutoCommit(true);
        }
    }

    /**
     * Deletes information from a specific table based on the specified condition.
     *
     * @param connection The Connection object for the database connection.
     * @param tableName  The name of the table to delete from.
     * @param conditionColumn The column used in the condition.
     * @param conditionValue The value to match in the condition.
     * @throws SQLException If a database error occurs.
     */
    private static void deleteFromTable(Connection connection, String tableName, String conditionColumn, int conditionValue) throws SQLException {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, conditionValue);
            deleteStatement.executeUpdate();
        }
    }
}
