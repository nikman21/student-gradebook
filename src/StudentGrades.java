/*
Name: Nikolas Manuel
Date: 04/24/2023
Description: simple multi-table database & application that keeps track of students and their grades.
Usage: Go to StudentGrades.java file and run it. You will be prompted to enter a number.
*/


import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

public class StudentGrades {
    public static void main(String[] args) {
        try {
            // Load the JDBC driver and connect to the database
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:classDB.db");
            // Load the data in the database into a collection of student objects
            ArrayList<Student> students = loadStudents(conn);

            // Print out the menu and get the user's choice
            int choice = 0;
            Scanner scanner = new Scanner(System.in);
            while (choice != 6) {
                System.out.println("Menu:");
                System.out.println("1. Display all students");
                System.out.println("2. Display all students and their grades");
                System.out.println("3. Display grades for a specific student");
                System.out.println("4. Display all students with last name like...");
                System.out.println("5. Calculate class average for a specific assignment");
                System.out.println("6. Exit");
                System.out.print("Enter your choice (1-6): ");
                choice = scanner.nextInt();

                // Execute the chosen query
                switch (choice) {
                    case 1:
                        displayAllStudents(students);
                        break;
                    case 2:
                        displayAllGrades(students);
                        break;
                    case 3:
                        displayStudentGrades(students);
                        break;
                    case 4:
                        displayStudentsWithLastNameLike(conn);
                        break;
                    case 5:
                        calculateClassAverage(conn);
                        break;
                    case 6:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
            // Clean up resources
            conn.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Load the data in the database into a collection of student objects
    private static ArrayList<Student> loadStudents(Connection conn) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM studentTable");
        while (rs.next()) {
            // Create a new student object and add assignments to it
            Student student = new Student(rs.getString("student_id"), rs.getString("first_name"), rs.getString("last_name"));
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM assnTable WHERE student_id = ?");
            ps.setString(1, rs.getString("student_id"));
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                Assignment assignment = new Assignment(rs2.getInt("assn_num"), rs2.getInt("earned_points"), rs2.getInt("max_points"), rs2.getString("letter_grade"));
                student.addAssignment(assignment);
            }
            rs2.close();
            ps.close();
            students.add(student);
        }
        rs.close();
        stmt.close();
        return students;
    }

    // Display all students
    private static void displayAllStudents(ArrayList<Student> students) {
        for (Student student : students) {
        	System.out.println(student.getLastName() + ", " + student.getFirstName() + "");
        	System.out.println("Student ID: " + student.getId());
        	System.out.println();
        	
        }
    }

    // Display all students and their grades
    private static void displayAllGrades(ArrayList<Student> students) {
        for (Student student : students) {
        	System.out.println(student.getLastName() + ", " + student.getFirstName() + ":");
            for (Assignment assignment : student.getAssignments()) {
            	System.out.println("Assignment " + assignment.getAssignmentNum() + ": " + assignment.getEarnedPoints() + "/" + assignment.getMaxPoints());
            }
        }
    }
    // Display grades for a specific student
    private static void displayStudentGrades(ArrayList<Student> students) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the student ID: ");
        String id = scanner.nextLine();
        boolean found = false;
        for (Student student : students) {
            if (student.getId().equals(id)) {
            	 System.out.println(student.getLastName() + ", " + student.getFirstName() + ":");
                for (Assignment assignment : student.getAssignments()) {
                	System.out.println("Assignment " + assignment.getAssignmentNum() + ": " + assignment.getEarnedPoints() + "/" + assignment.getMaxPoints());
                	System.out.println();
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Student not found.");
        }
    }

    // Display all students with last name like
    private static void displayStudentsWithLastNameLike(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the last name (use % for wildcard): ");
        String lastName = scanner.nextLine();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM studentTable WHERE last_name LIKE ?||'%'");
        ps.setString(1, lastName);
        ResultSet rs = ps.executeQuery();
        boolean found = false;
        while (rs.next()) {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
            found = true;
        }
        if (!found) {
            System.out.println("No students found with last name matching '" + lastName + "'");
            System.out.println();
        }
        rs.close();
        ps.close();
    }

    // Calculate class average for a specific assignments
    private static void calculateClassAverage(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the assignment number: ");
        int assignmentNum = scanner.nextInt();
        PreparedStatement ps = conn.prepareStatement("SELECT AVG(earned_points*1.0/max_points*1.0)*100 AS average FROM assnTable WHERE assn_num = ?");
        ps.setInt(1, assignmentNum);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	System.out.println("Class average for assignment " + assignmentNum + ": " + String.format("%.2f", rs.getDouble("average")));
        } else {
            System.out.println("Assignment not found.");
        }
        rs.close();
        ps.close();
    }
}

