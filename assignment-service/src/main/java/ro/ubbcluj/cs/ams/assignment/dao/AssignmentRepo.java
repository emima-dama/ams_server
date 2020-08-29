package ro.ubbcluj.cs.ams.assignment.dao;

import ro.ubbcluj.cs.ams.assignment.model.tables.pojos.Grade;
import ro.ubbcluj.cs.ams.assignment.model.tables.records.GradeRecord;

import java.util.List;

public interface AssignmentRepo {

    GradeRecord addGrade(Grade grade);

    List<GradeRecord> findAllByStudent(String student);

    List<GradeRecord> findAllByStudentAndSubject(String name, String subject);
}
