package edu.unsa.exam2.model;

public class StepsStorage {

    private static int stepsCount;

    public static void setStepsCount(int newValue) {
        stepsCount = newValue;
    }

    public static void incrementSteps() {
        stepsCount++;
    }

    public static int getStepsCount() {
        return stepsCount;
    }
}
