package ro.ubbcluj.cs.ams.assignment.service;

import ro.ubbcluj.cs.ams.assignment.dto.*;

public interface Service {

    GradeResponseDto addGrade(GradeDto gradeDto, String teacher);

    GradesResponseDto findGradesByStudent(String student);
}
