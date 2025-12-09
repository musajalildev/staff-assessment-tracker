package com.assessment.tracker.server.utils.enums;

public enum AssessmentType {
    EXAM("exam"), TEST_UNASSIGNED("test"), TEST_AUTOGRADED("tag"), TEST_SINGLE_MARKER("tsm"), TEST_TEAM_MARKER("ttm"), COURSEWORK("cw");
    private final String key;

    AssessmentType(String key) {this.key = key;}

    public static AssessmentType fromString(String inputType) {
        for (AssessmentType assessmentType : AssessmentType.values()) {
            if (assessmentType.key.equalsIgnoreCase(inputType)) {
                return assessmentType;
            }
        }
        throw new IllegalArgumentException("No enum for: " + inputType);
    }
}
