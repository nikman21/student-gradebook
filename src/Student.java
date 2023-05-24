/*
Name: Nikolas Manuel
Date: 04/24/2023
Description: simple multi-table database & application that keeps track of students and their grades.
Usage: Go to StudentGrades.java file and run it. You will be prompted to enter a number.
*/

import java.util.ArrayList;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private ArrayList<Assignment> assignments;

    public Student(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }
}

