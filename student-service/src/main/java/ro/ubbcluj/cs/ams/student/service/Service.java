package ro.ubbcluj.cs.ams.student.service;

import ro.ubbcluj.cs.ams.student.dto.*;

public interface Service {

    GroupResponseDto addGroup(GroupRequestDto groupRequestDto);

    boolean addEnrollment(EnrollmentRequestDto enrollmentRequestDto);

    StudentsBySubjectsResponseDto getStudentsBySubjects(String subjectId);

    SubjectsByStudentDto findSubjectsByStudent(String studentId);
}
