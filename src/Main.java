import java.sql.*;
import java.util.Scanner;

/**
 * This class represents the main entry point for the project management system.
 * It provides a menu-driven interface for interacting with project information.
 */
public class Main {

    // JDBC connection details
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/{your database name}?useSSL=false";
    static final String USER = "{user}";
    static final String PASSWORD = "{password}";

    /**
     * The main method that executes the project management system.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            try (Scanner scanner = new Scanner(System.in)) {

                // Infinite loop for continuous user interaction
                while (true) {
                    // Display menu options
                    System.out.println("1. Fetch Project Information");
                    System.out.println("2. Edit Project Information");
                    System.out.println("3. Add New Project");
                    System.out.println("4. Finalize Existing Project");
                    System.out.println("5. Find Incomplete Projects");
                    System.out.println("6. Find Overdue Projects");
                    System.out.println("7. Delete Project");
                    System.out.println("8. Exit");
                    System.out.print("Choose an option (1, 2, 3, 4, 5, 6, 7, or 8): ");

                    int option = scanner.nextInt();

                    if (option == 8) {
                        // Exit the loop if the user chooses option 8
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    }

                    int projectNum = 0; // Initialize to avoid compilation error

                    if (option != 3 && option != 5 && option != 6) {
                        // Take user input for project_num
                        System.out.print("Enter Project Number: ");
                        projectNum = scanner.nextInt();
                    }

                    switch (option) {
                        case 1:
                            // Fetch project info based on user input
                            ProjectInfoFetcher.fetchProjectInfo(connection, projectNum);
                            break;

                        case 2:
                            // Edit project info based on user input
                            ProjectInfoEditor.editProjectInfo(connection, projectNum);
                            break;

                        case 3:
                            // Add new project
                            ProjectInfoAdd.addNewProject(connection);
                            break;

                        case 4:
                            // Finalize existing project
                            FinalizeProject.finalizeExistingProject(connection, projectNum);
                            break;

                        case 5:
                            // Find all projects that still need to be completed
                            ProjectInfoFetcher.findIncompleteProjects(connection);
                            break;

                        case 6:
                            // Find all overdue projects
                            ProjectInfoFetcher.findOverdueProjects(connection);
                            break;

                        case 7:
                            // Delete project based on user input with confirmation
                            if (confirmDelete(scanner)) {
                                ProjectInfoDelete.deleteInfo(connection, projectNum);
                            } else {
                                System.out.println("Deletion canceled by user.");
                            }
                            break;

                        default:
                            System.out.println("Invalid option. Please choose 1, 2, 3, 4, 5, 6, 7, or 8.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user for confirmation of project deletion.
     *
     * @param scanner The Scanner object used for user input.
     * @return true if the user confirms deletion, false otherwise.
     */
    private static boolean confirmDelete(Scanner scanner) {
        System.out.print("Are you sure you want to delete this project? (yes/no): ");
        String confirmation = scanner.next().toLowerCase();
        return confirmation.equals("yes");
    }
}
