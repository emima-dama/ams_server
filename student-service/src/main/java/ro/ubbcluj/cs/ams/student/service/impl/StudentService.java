package ro.ubbcluj.cs.ams.student.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ro.ubbcluj.cs.ams.student.dao.enrollment.EnrollmentDao;
import ro.ubbcluj.cs.ams.student.dao.group.GroupDao;
import ro.ubbcluj.cs.ams.student.dao.student.StudentDao;
import ro.ubbcluj.cs.ams.student.dto.*;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.Enrollment;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;
import ro.ubbcluj.cs.ams.student.model.tables.records.EnrollmentRecord;
import ro.ubbcluj.cs.ams.student.model.tables.records.GroupUniversityRecord;
import ro.ubbcluj.cs.ams.student.service.Service;
import ro.ubbcluj.cs.ams.student.service.exception.StudentExceptionType;
import ro.ubbcluj.cs.ams.student.service.exception.StudentServiceException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@org.springframework.stereotype.Service
public class StudentService implements Service {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private StudentDao studentDao;

    private final Logger logger = LogManager.getLogger(StudentService.class);

    @Override
    public GroupResponseDto addGroup(GroupRequestDto groupRequestDto) {

        logger.info(" ========= LOGGING addGroup ==========");

        GroupUniversity groupUniversity = converetToGroupUniveristyPojo(groupRequestDto);
        Integer idGroup = groupDao.addGroup(groupUniversity);

        if(idGroup == null){
            logger.error(" ======== ERROR addGroup ======== ");
            throw new StudentServiceException("Already exists a group with name = " + groupRequestDto.getName() + " and specId = " + groupRequestDto.getSpecId(), StudentExceptionType.DUPLICATE_GROUP, HttpStatus.BAD_REQUEST);
        }

        logger.info(" ========== SUCCESSFUL LOGGING addGroup =========== ");
        return new GroupResponseDto(idGroup);
    }

    @Override
    public boolean addEnrollment(EnrollmentRequestDto enrollmentRequestDto) {

        Enrollment enrollment = convertToEnrollmentPojo(enrollmentRequestDto);
        EnrollmentRecord enrollmentRecord = enrollmentDao.addEnrollment(enrollment);

        if(enrollmentRecord == null){
            logger.info("======== ERROR addEnrollment ===========");
            throw new StudentServiceException("Already exists a enrollment with studentId = " + enrollment.getStudentId() + " and subjectId = " + enrollment.getSubjectId(), StudentExceptionType.DUPLICATE_ENROLLMENT, HttpStatus.BAD_REQUEST);
        }

        logger.info(" ============ SUCCESSFUL LOGGING addEnrollment ==============");
        return true;
    }

    @Override
    public StudentsBySubjectsResponseDto getStudentsBySubjects(String subjectId) {

        logger.info(" ============ LOGGING getStudentsBySubjects ===========");

        List<String> students = enrollmentDao.getStundentsBySubject(subjectId);

        StudentsBySubjectsResponseDto studentsReponse = StudentsBySubjectsResponseDto.builder()
                .usernames(new ArrayList<>(new HashSet<>(students))) //without duplicates
                .build();
        logger.info(" ============ SUCCESSFUL LOGGING getStudentsBySubjects with size {} ============",studentsReponse.getUsernames().size());
        return studentsReponse;
    }

    @Override
    public SubjectsByStudentDto findSubjectsByStudent(String studentId) {

        logger.info(" ============ LOGGING findSubjectsByStudent ===========");

        List<String> subjects = enrollmentDao.getSubjectsByStudent(studentId);

        SubjectsByStudentDto subjectsByStudentDto = SubjectsByStudentDto.builder()
                .subjectsIds(new ArrayList<>(new HashSet<>(subjects))) //without duplicates
                .build();
        logger.info(" ============ SUCCESSFUL LOGGING findSubjectsByStudent with size {} ============",subjectsByStudentDto.getSubjectsIds().size());
        return subjectsByStudentDto;
    }

    @Override
    public GroupNameResponseDto findGroupByStudent(Principal studentId) {

        logger.info(">>>>>>>>>> LOGGING findGroupByStudent <<<<<<<<<<<<<<<<");

        GroupUniversityRecord groupUniversityRecord = groupDao.findOneByStudent(studentId.getName());
        String groupName = groupUniversityRecord.getName();

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING findGroupByStudent {}<<<<<<<<<<<<<<<<", groupName);
        return GroupNameResponseDto.builder()
                .name(groupName).build();
    }

    @Override
    public GroupsResponseDto getAllGroups() {

        logger.info(">>>>>>>>>> LOGGING getAllGroups <<<<<<<<<<<<<<<<");

        List<GroupUniversityRecord> groups = groupDao.findAll();

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING getAllGroups <<<<<<<<<<<<<<<<");
        return GroupsResponseDto.builder()
                .groups(groupMapper.groupUniversityRecordsToGroupUniversities(groups))
                .build();

    }

    @Override
    public StudentsByGroupResponseDto findStudentsByGroup(Integer groupId) {

        logger.info(">>>>>>>>>> LOGGING findStudentsByGroup <<<<<<<<<<<<<<<<");

        List<String> students = studentDao.findStudentsByGroup(groupId);

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING findStudentsByGroup <<<<<<<<<<<<<<<<");
        return StudentsByGroupResponseDto
                .builder()
                .usernames(students)
                .build();

    }

    GroupUniversity converetToGroupUniveristyPojo(GroupRequestDto groupRequestDto){

        return new GroupUniversity(-1, groupRequestDto.getName(), groupRequestDto.getSpecId(), groupRequestDto.getYear());
    }

    Enrollment convertToEnrollmentPojo(EnrollmentRequestDto enrollmentRequestDto){

        return new Enrollment(enrollmentRequestDto.getStudentUsername(),enrollmentRequestDto.getSubjectId());
    }
}
