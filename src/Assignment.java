/*
Name: Nikolas Manuel
Date: 04/24/2023
Description: simple multi-table database & application that keeps track of students and their grades.
Usage: Go to StudentGrades.java file and run it. You will be prompted to enter a number.
*/

public class Assignment {
    private int assignmentNum;
    private int earnedPoints;
    private int maxPoints;

    public Assignment(int assignmentNum, int earnedPoints, int maxPoints, String letterGrade) {
        this.assignmentNum = assignmentNum;
        this.earnedPoints = earnedPoints;
        this.maxPoints = maxPoints;
    }

    public int getAssignmentNum() {
        return assignmentNum;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }
}
