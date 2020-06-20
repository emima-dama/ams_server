package ro.ubbcluj.cs.ams.assignment.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ro.ubbcluj.cs.ams.assignment.dao.AssignmentRepo;
import ro.ubbcluj.cs.ams.assignment.dto.GradeDto;
import ro.ubbcluj.cs.ams.assignment.dto.*;
import ro.ubbcluj.cs.ams.assignment.dto.GradeResponseDto;
import ro.ubbcluj.cs.ams.assignment.model.tables.pojos.Grade;
import ro.ubbcluj.cs.ams.assignment.model.tables.records.GradeRecord;
import ro.ubbcluj.cs.ams.assignment.service.Service;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceException;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceExceptionType;

import javax.inject.Provider;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@org.springframework.stereotype.Service
public class AssignmentService implements Service {

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private Provider<Instant> provider;

    @Autowired
    private AssignmentRepo assignmentRepo;

    private final Logger logger = LogManager.getLogger(AssignmentService.class);

    @Override
    public GradeResponseDto addGrade(GradeDto gradeDto, String teacher) {

        logger.info(">>>>>>>>>>>>>>>> LOGGING addGrade <<<<<<<<<<<<<");

        Grade grade = gradeMapper.gradeDtoToGrade(gradeDto);
        grade.setTeacher(teacher);
        grade.setDate(LocalDateTime.ofInstant(provider.get(), ZoneId.systemDefault()));
        GradeRecord gradeRecord = assignmentRepo.addGrade(grade);

        logger.info(">>>>>>>>>>>>>>>> SUCCESSFUL LOGGING addGrade <<<<<<<<<<<<<");
        return gradeMapper.gradeRecordToGradeResponseDto(gradeRecord);
    }

    @Override
    public GradesResponseDto findGradesByStudent(String student) {

        logger.info(">>>>>>>>>>>>>>>> LOGGING findGradesByStudent <<<<<<<<<<<<<");

        List<GradeRecord> gradeRecords = assignmentRepo.findAllByStudent(student);
        if (gradeRecords == null){
            throw new AssignmentServiceException("Connection with db failed or there is no grades", AssignmentServiceExceptionType.ERROR, HttpStatus.NOT_FOUND);
        }

        logger.info(">>>>>>>>>>>>>>>> SUCCESSFUL LOGGING addGrade <<<<<<<<<<<<<");
        return GradesResponseDto.builder()
//                .grades(gradeRecords)
                .grades(gradeMapper.gradeRecordstoGradeResponseDtos(gradeRecords))
                .build();
    }
}
