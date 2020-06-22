package ro.ubbcluj.cs.ams.subject.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import ro.ubbcluj.cs.ams.subject.dao.activityTypeDao.ActivityTypeDao;
import ro.ubbcluj.cs.ams.subject.dao.spLinkDao.SpLinkDao;
import ro.ubbcluj.cs.ams.subject.dao.specializationDao.SpecializationDao;
import ro.ubbcluj.cs.ams.subject.dao.subjectCodeDao.SubjectCodeDao;
import ro.ubbcluj.cs.ams.subject.dao.subjectDao.SubjectDao;
import ro.ubbcluj.cs.ams.subject.dto.*;
import ro.ubbcluj.cs.ams.subject.model.tables.pojos.Subject;
import ro.ubbcluj.cs.ams.subject.model.tables.pojos.SubjectCode;
import ro.ubbcluj.cs.ams.subject.model.tables.records.*;
import ro.ubbcluj.cs.ams.subject.service.Service;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectExceptionType;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectServiceException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Service
public class SubjectService implements Service {

    private final Logger logger = LogManager.getLogger(SubjectService.class);

    @Autowired
    private SpecializationDao specializationDao;

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private SubjectCodeDao subjectCodeDao;

    @Autowired
    private SpLinkDao spLinkDao;

    @Autowired
    private SpLinkMapper spLinkMapper;

    @Autowired
    private SpecializationMapper specializationMapper;

    @Autowired
    private ActivityTypeDao activityTypeDao;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private ActivityMapper activityMapper;

    private String prefixCode = "ML";

    private DecimalFormat myFormat = new DecimalFormat("0000");


    @Transactional
    @Override
    public SubjectResponseDto addSubject(SubjectRequestDto subjectRequestDto) {

        logger.info("+++++++ LOGGING addSubject +++++++");

//        SpecializationRecord specialization = specializationDao.findById(subjectRequestDto.getSpecId());
        Subject subject = new Subject("", subjectRequestDto.getName(), subjectRequestDto.getCredits(), subjectRequestDto.getSpecId(), subjectRequestDto.getYear());
        String subjectId = generateSubjectCode(subject);
        subject.setId(subjectId);

        SubjectRecord subjectSave = subjectDao.addSubject(subject);

        if (subjectSave == null)
            throw new SubjectServiceException("Subject \"" + subject.getName() + "\" already exists!!", SubjectExceptionType.DUPLICATE_SUBJECT, HttpStatus.BAD_REQUEST);

        logger.info("+++++++ SUCCESSFUL LOGGING addSubject +++++++");
        return subjectMapper.subjectRecordToSubjectResponseDto(subjectSave);
    }

    @Override
    public SpLinkResponseDto findSpLink(String subjectId, int type, String professor) {

        logger.info("========== LOGGING findSpLink ==========");

        SpLinkRecord spLinkRecord = spLinkDao.findSpLink(subjectId, type, professor);

        logger.info("========== SUCCESSFUL LOGGING findSpLink ==========");
        return spLinkMapper.spLinkRecordToSpLinkResponseDto(spLinkRecord);
    }

    @Override
    public SubjectsResponseDto findSubjectsByTeacher(String teacher) {
        logger.info("========== LOGGING findSubjectByTeacher ==========");

        List<SpLinkRecord> spLinkRecords = spLinkDao.findSpLinkByUser(teacher);
        List<SubjectRecord> subjectRecords = new ArrayList<>();

        for(SpLinkRecord spLinkRecord : spLinkRecords){
            SubjectRecord subjectRecord = subjectDao.findById(spLinkRecord.getSubjectId());
            if (!subjectRecords.contains(subjectRecord)){
                subjectRecords.add(subjectRecord);
            }
        }
        SubjectsResponseDto subjectsResponseDto = SubjectsResponseDto.builder()
                .subjects(subjectMapper.subjectRecordsToSubjectResponseDtos(subjectRecords))
                .build();
        logger.info("========== SUCCESSFUL LOGGING findSubjectByTeacher with size {} ==========", subjectsResponseDto.getSubjects().size());
        return subjectsResponseDto;
    }

    @Override
    public SpecializationResponseDto findSpecById(Integer specId) {

        logger.info("========== LOGGING findSpecById ==========");
        SpecializationRecord specializationRecord = specializationDao.findById(specId);
        logger.info("========== SUCCESSFUL LOGGING findSpecById {} - {} - {}==========",specializationRecord.getId(),specializationRecord.getName(),specializationRecord.getLanguage());
        return specializationMapper.specializationRecordToSpecializationResponseDto(specializationRecord);

    }

    @Override
    public SubjectResponseDto findSubjectById(String id) {

        logger.info("========== LOGGING findSubjectById ==========");
        SubjectRecord subjectRecord = subjectDao.findById(id);
        logger.info("========== SUCCESSFUL LOGGING findSubjectById ================");
        return subjectMapper.subjectRecordToSubjectResponseDto(subjectRecord);
    }

    @Override
    public ActivitiesResponseDto findActivitiesByTeacher(String name) {

        logger.info(">>>>>>>>>> LOGGING findActivitiesByTeacher {}<<<<<<<",name);
        List<SpLinkRecord> spLinkRecords = spLinkDao.findSpLinkByUser(name);
        List<ActivityTypeRecord> activityTypeRecords = new ArrayList<>();

        for(SpLinkRecord spLinkRecord: spLinkRecords){
            ActivityTypeRecord activityTypeRecord = activityTypeDao.findById(spLinkRecord.getTypeId());
            if(!activityTypeRecords.contains(activityTypeRecord))
                activityTypeRecords.add(activityTypeRecord);
        }
        logger.info("========== SUCCESSFUL LOGGING findActivitiesByTeacher with size {} ================",activityTypeRecords.size());

        ActivitiesResponseDto activitiesResponseDto = ActivitiesResponseDto.builder()
                .activities(activityMapper.activityTypeRecordsToActivityResponseDtos(activityTypeRecords))
                .build();
        return activitiesResponseDto;

    }

    @Override
    public SubjectsResponseDto findSubjectsByStudent(String studentId,SubjectsByStudentDto subjectsByStudent) {

        logger.info("========== LOGGING findSubjectsByStudent {} ==========",studentId);

        List<SubjectRecord> subjectRecords = new ArrayList<>();
        List<String> subjects = subjectsByStudent.getSubjectsIds();

        for(String id: subjects){
            SubjectRecord currentSubject = subjectDao.findById(id);
            subjectRecords.add(currentSubject);
        }
        logger.info("========== SUCCESSFUL LOGGING findSubjectsByStudent with size {} ================",subjectRecords.size());

        return SubjectsResponseDto.builder()
                .subjects(subjectMapper.subjectRecordsToSubjectResponseDtos(subjectRecords))
                .build();
    }

    @Override
    public SubjectActivityResponseDto findSubjectActivityByIds(String courseId, Integer activityId) {

        logger.info("========== LOGGING findSubjectActivityByIds {} {} ==========",courseId, activityId);

        String subjectName = subjectDao.findNameById(courseId);
        if(subjectName.isEmpty())
            throw new SubjectServiceException("There is no subject with id "+courseId,SubjectExceptionType.SUBJECT_NOT_FOUND,HttpStatus.NOT_FOUND);

        String activityName = activityTypeDao.findNameById(activityId);
        if(activityName.isEmpty())
            throw new SubjectServiceException("There is no activity with id "+activityId,SubjectExceptionType.ACTIVITY_TYPE_NOT_FOUND,HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findSubjectActivityByIds {} {} ================",subjectName,activityName);

        return SubjectActivityResponseDto.builder()
                .subjectName(subjectName)
                .activityName(activityName)
                .build();
    }

    private String generateSubjectCode(Subject subject) {

        SpecializationRecord specialization = specializationDao.findById(subject.getSpecId());
        if (Objects.isNull(specialization)) {
            logger.info("Specialization not found!");
            throw new SubjectServiceException("Specialization not found!", SubjectExceptionType.SPECIALIZATION_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        String code = prefixCode + specialization.getLanguage().charAt(0);

        SubjectCodeRecord scr = subjectCodeDao.findBySubjectName(subject.getName());

        if (scr == null) { // this subject does not exist

            Integer intCode = subjectCodeDao.addSubjectCode(new SubjectCode(null, subject.getName()));
            String stringCode = myFormat.format(intCode);
            code += stringCode;
        } else {
            code += scr.getCode();
        }
        return code;
    }

}
