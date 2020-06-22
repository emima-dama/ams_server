package ro.ubbcluj.cs.ams.student.service;

import ro.ubbcluj.cs.ams.student.dto.*;

import java.security.Principal;

public interface Service {

    GroupResponseDto addGroup(GroupRequestDto groupRequestDto);

    boolean addEnrollment(EnrollmentRequestDto enrollmentRequestDto);

    StudentsBySubjectsResponseDto getStudentsBySubjects(String subjectId);

    SubjectsByStudentDto findSubjectsByStudent(String studentId);

    GroupNameResponseDto findGroupByStudent(Principal studentId);

    GroupsResponseDto getAllGroups();

    StudentsByGroupResponseDto findStudentsByGroup(Integer groupId);
}
