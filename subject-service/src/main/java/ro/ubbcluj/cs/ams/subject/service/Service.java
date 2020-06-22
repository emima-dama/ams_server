package ro.ubbcluj.cs.ams.subject.service;

import ro.ubbcluj.cs.ams.subject.dto.*;

public interface Service {

    SubjectResponseDto addSubject(SubjectRequestDto subject);

    SpLinkResponseDto findSpLink(String subjectId, int type, String professor);

    SubjectsResponseDto findSubjectsByTeacher(String teacher);

    SpecializationResponseDto findSpecById(Integer specId);

    SubjectResponseDto findSubjectById(String id);

    ActivitiesResponseDto findActivitiesByTeacher(String name);

    SubjectsResponseDto findSubjectsByStudent(String studentId,SubjectsByStudentDto subjectsByStudent);

    SubjectActivityResponseDto findSubjectActivityByIds(String courseId, Integer activityId);
}
