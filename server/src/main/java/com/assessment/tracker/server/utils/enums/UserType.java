package com.assessment.tracker.server.utils.enums;

public enum UserType {
    ROLE_TEACHING_SUPPORT,
    ROLE_ACADEMIC,
    ROLE_EXTERNAL_EXAMINER,
    ROLE_EXAMS_OFFICER;

    public String toString() {
        String pureExtract=this.name();
        String[] splitted=pureExtract.split("_");
        String a,b;
        if(splitted.length == 2)
            {return splitted[1];}
        else
             a = splitted[1]; b = splitted[2];
             return a + " " + b;
    }
}