package com.ahuzmeza.my_app.Helpers;

import java.lang.reflect.Array;
import java.util.List;

public class Subject {

    private String  subjectName;
    private int     subjectAverage;

    public Subject(String _subjectName, int _subjectAverage) {
        subjectName = _subjectName;
        subjectAverage = _subjectAverage;
    }

    public Subject getSubject() {
        return (new Subject(subjectName, subjectAverage));
    }

    public String getSubjectName() {
        return (subjectName);
    }

    public int getSubjectAverage() {
        return subjectAverage;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSubjectAverage(int subjectAverage) {
        this.subjectAverage = subjectAverage;
    }

    public boolean sameNameExists(List<Subject> haystack) {
        for (Subject subj: haystack) {
            if (subj.getSubjectName().equals( this.getSubjectName()))
                return true;
        }
        return  false;
    }

}
