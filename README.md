# Project Management System

- [Overview](#overview)
  - [Features](#features)
- [Getting Started](#getting-started)
- [Author](#author)

# Overview

This a mock project management system created for a fictional construction company. This project is terminal based and was built in Java and MySQL. The PMS will assist with logging and keeping track of various construction projects.

## Features

This terminal based project is packed with multiple extremely usefully features which are listed below, some along with their respective code blocks.

- Adding Project

  - The "ProjectInfoAdd" class is responsible for adding new project information to the database. The user is able to create a new customer/contractor/architect or reference a customer/contractor/architect already present in the database by using the respective IDs.

- Editing existing project

  - The "ProjectInfoEditor" class is responsible for editing any existing project's information in the database.The user has the ability to select what type of data they would like to edit(project/customer/contractor/architect).As with the "ProjectInfoAdd" class the user is able to create a new customer/contractor/architect or reference a customer/contractor/architect already present in the database by using the respective IDs.

- Delete project information

  - The "ProjectInfoDelete" allows the user to delete project information from the database.

    ```java
    private static void deleteFromTable(Connection connection, String tableName, String conditionColumn, int conditionValue) throws SQLException {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, conditionValue);
            deleteStatement.executeUpdate();
        }
    }
    ```

- Finalize Project

  - The "FinalizedProject" class is responsible for finalizing projects and automatically adds the compilation date as the current.

    ```java
    private static void markProjectAsFinalized(Connection connection, int projectNum) throws SQLException {
        String query = "UPDATE PoisePMS SET Finalized = true WHERE project_num = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, projectNum);
        preparedStatement.executeUpdate();
        }
    }
    ```

- Project information fetcher

  - The "ProjectInfoFetcher" class consists of multiple methods which are responsible for fetching various types of information from the database

  - The "printProjectInformation" Method is responsible for printing all the information for a given project.

  - The "findOverdueProjects" Method is responsible for gathering all the overdue projects.

  - The "findIncompleteProjects" Method is responsible for gathering all the incomplete projects.

  ```java
  public static void findIncompleteProjects(Connection connection) {
        try {
            String query = "SELECT * FROM PoisePMS WHERE finalized IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        System.out.println("Project Number: " + resultSet.getInt("project_num"));
                        System.out.println("Project Name: " + resultSet.getString("project_name") + "\n");
                    } while (resultSet.next());
                } else {
                    System.out.println("No projects need to be completed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
  }
  ```
"# Project-Management-System" 
