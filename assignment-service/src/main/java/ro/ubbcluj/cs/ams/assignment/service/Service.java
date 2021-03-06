package ro.ubbcluj.cs.ams.assignment.service;

import ro.ubbcluj.cs.ams.assignment.dto.GradeDto;
import ro.ubbcluj.cs.ams.assignment.dto.GradeResponseDto;
import ro.ubbcluj.cs.ams.assignment.dto.GradesResponseDto;

public interface Service {

    GradeResponseDto addGrade(GradeDto gradeDto, String teacher);

    GradesResponseDto findGradesByStudent(String student);

    GradesResponseDto findGradesBySubject(String subject, String name);
}
